package org.co.taplink.users.services;

import lombok.NonNull;
import org.co.taplink.users.modals.AuthResponse;
import org.co.taplink.users.modals.LoginRequest;
import org.co.taplink.users.modals.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<@NonNull AuthResponse> register(RegisterRequest request);
    ResponseEntity<@NonNull AuthResponse> login(LoginRequest request);
}
