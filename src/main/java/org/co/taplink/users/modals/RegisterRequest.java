package org.co.taplink.users.modals;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
) {}
