package org.co.taplink.users.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.co.taplink.users.modals.AuthResponse;
import org.co.taplink.users.modals.LoginRequest;
import org.co.taplink.users.modals.RegisterRequest;
import org.co.taplink.users.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<@NonNull AuthResponse> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<@NonNull AuthResponse>(this.authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<@NonNull AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }
}
