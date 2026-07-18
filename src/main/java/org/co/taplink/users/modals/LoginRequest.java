package org.co.taplink.users.modals;

public record LoginRequest(
        String username,
        String password
) {}
