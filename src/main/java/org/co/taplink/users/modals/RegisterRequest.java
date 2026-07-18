package org.co.taplink.users.modals;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
