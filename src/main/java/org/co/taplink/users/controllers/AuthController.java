package org.co.taplink.users.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.co.taplink.users.modals.AuthResponse;
import org.co.taplink.users.modals.LoginRequest;
import org.co.taplink.users.modals.RegisterRequest;
import org.co.taplink.users.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.AUTH_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH_PATH)
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<@NonNull AuthResponse> register(@RequestBody RegisterRequest request) {
        return this.authService.register(request);
    }

    @PostMapping("login")
    public ResponseEntity<@NonNull AuthResponse> login(@RequestBody LoginRequest request) {
        return this.authService.login(request);
    }
}
