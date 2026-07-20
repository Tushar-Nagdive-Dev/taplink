package org.co.taplink.configs.security;

import org.co.taplink.configs.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.*;
import static org.co.taplink.utils.TapLinkAppConstants.FORWARD_SLASH;
import static org.co.taplink.utils.TapLinkAppConstants.MATCH_ALL;
import static org.co.taplink.utils.TapLinkAppConstants.ROLES.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AUTH_PATH + FORWARD_SLASH + MATCH_ALL,
                                SWAGGER_UI_PATH + FORWARD_SLASH + MATCH_ALL,
                                SWAGGER_HTML_PATH + FORWARD_SLASH + MATCH_ALL,
                                V3_API_DOCS_PATH).permitAll()
                        .requestMatchers(ADMIN_PATH + FORWARD_SLASH + MATCH_ALL).hasRole(ADMIN)
                        .requestMatchers(PREMIUM_USER_PATH + FORWARD_SLASH + MATCH_ALL).hasAnyRole(USER, PREMIUM)
                        .requestMatchers(LINKS_PATH + FORWARD_SLASH + MATCH_ALL).hasAnyRole(USER, ADMIN)
                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
