package org.co.taplink.users.services.impl;

import lombok.RequiredArgsConstructor;
import org.co.taplink.configs.jwt.JwtService;
import org.co.taplink.users.entities.Roles;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.modals.AuthResponse;
import org.co.taplink.users.modals.LoginRequest;
import org.co.taplink.users.modals.RegisterRequest;
import org.co.taplink.users.repository.RolesRepository;
import org.co.taplink.users.repository.UsersRepository;
import org.co.taplink.users.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.co.taplink.users.utils.TapLinkAppConstants.BEARER;
import static org.co.taplink.users.utils.TapLinkAppConstants.ROLE_USER;
import static org.co.taplink.users.utils.TapLinkAppMessages.Auth.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        if(this.usersRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException(String.format(USERNAME_TAKEN, request.username()));
        }

        Users user = new Users();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode(request.password()));

        Roles userRole = rolesRepository.findByName(ROLE_USER).orElseThrow(() -> new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND));
        user.addRole(userRole);
        this.usersRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, BEARER);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        Users user = this.usersRepository.findByUsernameWithRoles(request.username()).orElseThrow(() -> new IllegalArgumentException(INVALID_USER));
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, BEARER);
    }
}
