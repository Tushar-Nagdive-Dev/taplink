package org.co.taplink.configs.security;

import org.co.taplink.configs.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.co.taplink.utils.TapLinkAppConstants.*;
import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.*;
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
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Permit Angular SPA Static Resources & Root Pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/*.js",
                                "/*.css",
                                "/*.ico",
                                "/assets/**",
                                "/public/**"
                        ).permitAll()
                        // 2. Permit Angular Client-Side SPA Routes (handled by SpaController)
                        .requestMatchers(
                                "/signin",
                                "/signup",
                                "/auth-error",
                                "/taplink-dashboard/**",
                                "/user-profile/**"
                        ).permitAll()
                        // 3. Public Backend API Endpoints & Swagger
                        .requestMatchers(
                                AUTH_PATH + FORWARD_SLASH + MATCH_ALL,
                                SWAGGER_UI_PATH + FORWARD_SLASH + MATCH_ALL,
                                SWAGGER_HTML_PATH + FORWARD_SLASH + MATCH_ALL,
                                V3_API_DOCS_PATH
                        ).permitAll()
                        // 4. Role-Secured Backend API Endpoints
                        .requestMatchers(ADMIN_PATH + FORWARD_SLASH + MATCH_ALL).hasRole(ADMIN)
                        .requestMatchers(
                                PREMIUM_USER_PATH + FORWARD_SLASH + MATCH_ALL,
                                QR_BARCODE_PATH + FORWARD_SLASH + MATCH_ALL,
                                USER_PROFILE_PATH + FORWARD_SLASH + MATCH_ALL
                        ).hasAnyRole(USER, PREMIUM)
                        .requestMatchers(LINKS_PATH + FORWARD_SLASH + MATCH_ALL).hasAnyRole(USER, ADMIN)
                        // 5. Secure Everything Else
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:1005"));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(), HttpMethod.HEAD.name(),
                HttpMethod.OPTIONS.name(), HttpMethod.PATCH.name(),
                HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(FORWARD_SLASH+MATCH_ALL, configuration);
        return source;
    }
}
