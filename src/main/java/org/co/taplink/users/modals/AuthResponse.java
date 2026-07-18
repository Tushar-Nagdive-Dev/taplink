package org.co.taplink.users.modals;

public record AuthResponse(
        String accessToken,
        String tokenType // Usually "Bearer"
) {}
