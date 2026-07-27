package org.co.taplink.users.modals;

import java.util.Set;

public record SessionResponse(
        Boolean authenticated,
        Long userId,
        String username,
        String email,
        String fistName,
        String lastName,
        Set<String> roles
) {}
