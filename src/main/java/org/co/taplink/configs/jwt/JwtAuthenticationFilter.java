package org.co.taplink.configs.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.exception.SessionExpiredException;
import org.co.taplink.configs.exception.UserSessionNotFoundException;
import org.co.taplink.configs.security.TokenBlocklistService;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.services.CustomUserDetailsService;
import org.co.taplink.users.services.UserSessionService;
import org.co.taplink.utils.CookieUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.co.taplink.utils.TapLinkAppConstants.*;
import static org.co.taplink.utils.TapLinkAppMessages.Auth.SESSION_EXPIRED;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlocklistService tokenBlocklistService;
    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.info("==================================================");
        log.info("Incoming Request : {} {}", request.getMethod(), request.getRequestURI());

        try {

            // ------------------------------------------------------------
            // Extract JWT
            // ------------------------------------------------------------
            final String jwt = extractJwt(request);

            log.info("JWT Present : {}", jwt != null);

            if (jwt == null) {
                log.info("No JWT found. Continuing filter chain.");
                filterChain.doFilter(request, response);
                return;
            }

            // ------------------------------------------------------------
            // Parse JWT
            // ------------------------------------------------------------
            final UUID sessionId = jwtService.extractSessionId(jwt);
            final String username = jwtService.extractUsername(jwt);

            log.info("JWT Username  : {}", username);
            log.info("JWT SessionId : {}", sessionId);

            // ------------------------------------------------------------
            // Blocklist
            // ------------------------------------------------------------
            if (tokenBlocklistService.isBlocked(jwt)) {
                log.warn("JWT is blocklisted.");
                throw new SessionExpiredException(SESSION_EXPIRED);
            }

            // ------------------------------------------------------------
            // Database Session Validation
            // ------------------------------------------------------------
            log.info("Loading UserSession...");

            final UserSession session = userSessionService.findBySessionId(sessionId)
                    .orElseThrow(() -> {
                        log.error("UserSession NOT FOUND : {}", sessionId);
                        return new UserSessionNotFoundException();
                    });

            log.info("Session Found");
            log.info("Database SessionId : {}", session.getSessionId());
            log.info("Active             : {}", session.getIsActive());
            log.info("Expires At         : {}", session.getExpiresAt());

            if (!Boolean.TRUE.equals(session.getIsActive())) {
                log.warn("Session is inactive.");
                throw new SessionExpiredException(SESSION_EXPIRED);
            }

            if (session.getExpiresAt() != null &&
                    session.getExpiresAt().isBefore(LocalDateTime.now())) {

                log.warn("Session expired.");
                throw new SessionExpiredException(SESSION_EXPIRED);
            }

            // ------------------------------------------------------------
            // Spring Security Authentication
            // ------------------------------------------------------------
            Authentication existing =
                    SecurityContextHolder.getContext().getAuthentication();

            log.info("Existing Authentication : {}", existing);

            if (username != null &&
                    (existing == null || !existing.isAuthenticated())) {

                log.info("Loading UserDetails...");

                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                log.info("UserDetails Class : {}", userDetails.getClass().getName());

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    Authentication auth =
                            SecurityContextHolder.getContext().getAuthentication();

                    log.info("Authentication stored successfully.");
                    log.info("Authentication Class : {}", auth.getClass().getName());
                    log.info("Principal Class      : {}", auth.getPrincipal().getClass().getName());
                    log.info("Principal            : {}", auth.getPrincipal());
                    log.info("Authenticated        : {}", auth.isAuthenticated());

                } else {
                    log.warn("JWT validation failed.");
                }

            } else {
                log.info("Authentication already exists.");
            }

            log.info("Proceeding to controller...");
            filterChain.doFilter(request, response);

        } catch (SessionExpiredException |
                 UserSessionNotFoundException |
                 JwtException ex) {

            log.error("Authentication failed : {}", ex.getMessage(), ex);

            clearAuthentication(response);
        }
    }

    private String extractJwt(HttpServletRequest request) {
        return CookieUtils.getCookiesValue(request, TAPLINK_TOKEN);
    }

    private void clearAuthentication(HttpServletResponse response) throws IOException {

        log.info("Clearing SecurityContext");

        SecurityContextHolder.clearContext();

        ResponseCookie deleteCookie = ResponseCookie
                .from(TAPLINK_TOKEN, EMPTY_STRING)
                .httpOnly(true)
                .secure(false)
                .path(FORWARD_SLASH)
                .sameSite(STRICT)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, SESSION_EXPIRED);
    }
}