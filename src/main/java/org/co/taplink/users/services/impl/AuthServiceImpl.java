package org.co.taplink.users.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.exception.UserSessionNotFoundException;
import org.co.taplink.configs.jwt.JwtService;
import org.co.taplink.configs.security.SecurityUtils;
import org.co.taplink.configs.security.TokenBlocklistService;
import org.co.taplink.users.entities.Roles;
import org.co.taplink.users.entities.UserProfile;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.modals.AuthResponse;
import org.co.taplink.users.modals.LoginRequest;
import org.co.taplink.users.modals.RegisterRequest;
import org.co.taplink.users.modals.SessionResponse;
import org.co.taplink.users.repository.RolesRepository;
import org.co.taplink.users.repository.UsersRepository;
import org.co.taplink.users.services.AuthService;
import org.co.taplink.users.services.UserSessionService;
import org.co.taplink.utils.CookieUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.co.taplink.utils.TapLinkAppConstants.*;
import static org.co.taplink.utils.TapLinkAppMessages.Auth.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserSessionService sessionService;
    private final HttpServletRequest request;
    private final TokenBlocklistService tokenBlocklistService;

    @Transactional
    @Override
    public ResponseEntity<@NonNull AuthResponse> register(RegisterRequest request) {
        if(this.usersRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException(String.format(USERNAME_TAKEN, request.username()));
        }

        Users user = new Users();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode(request.password()));

        Roles userRole = rolesRepository.findByName(ROLE_USER).orElseThrow(() -> new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND));
        user.addRole(userRole);

        UserProfile defaultProfile = new UserProfile();
        defaultProfile.setFirstName(request.firstName());
        defaultProfile.setLastName(request.lastName());
        defaultProfile.setTimezone("UTC");
        user.setUserProfile(defaultProfile);

        this.usersRepository.save(user);

        return ResponseEntity.ok(new AuthResponse(REGISTER_SUCCESS));
    }

    @Override
    public ResponseEntity<@NonNull AuthResponse> login(LoginRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        Users user = this.usersRepository.findByUsernameWithRoles(request.username()).orElseThrow(() -> new IllegalArgumentException(INVALID_USER));

        UserSession userSession = this.sessionService.createSession(user, this.request);

        String jwtToken = jwtService.generateToken(user, userSession);
        ResponseCookie jwtCookie = ResponseCookie.from(TAPLINK_TOKEN, jwtToken).httpOnly(true)
                .secure(false) //Set to TRUE in production when you have HTTPS!
                .path(FORWARD_SLASH).maxAge(EXPIRE_7_DAYS) // Expires in 7 days
                .sameSite(STRICT).build(); // Protects against Cross-Site Request Forgery (CSRF)

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new AuthResponse(LOGIN_SUCCESS));
    }

    @Override
    @Transactional
    public ResponseEntity<@NonNull AuthResponse> logout(HttpServletRequest request) {
        String jwt = CookieUtils.getCookiesValue(request, TAPLINK_TOKEN);
        if (jwt != null) {
            UUID sessionId = jwtService.extractSessionId(jwt);
            sessionService.deactivateSession(sessionId);
            tokenBlocklistService.blockToken(jwt);
        }

        ResponseCookie deleteCookie = ResponseCookie.from(TAPLINK_TOKEN, EMPTY_STRING)
                .httpOnly(true).secure(false).path(FORWARD_SLASH)
                .sameSite(STRICT).maxAge(0).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body(new AuthResponse(SUCCESSFUL_LOGOUT));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SessionResponse> session() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        log.info("============= SESSION ENDPOINT =============");
        log.info("Authentication : {}", authentication);

        if (authentication != null) {
            log.info("Authentication Type : {}", authentication.getClass().getName());
            log.info("Principal Type      : {}", authentication.getPrincipal().getClass().getName());
            log.info("Principal           : {}", authentication.getPrincipal());
            log.info("Authenticated       : {}", authentication.isAuthenticated());
        }
        Users user = SecurityUtils.getCurrentUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SessionResponse response = new SessionResponse(
                true,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile().getFirstName(),
                user.getUserProfile().getLastName(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(response);
    }
}
