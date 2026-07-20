package org.co.taplink.configs.security;

import org.co.taplink.users.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.co.taplink.utils.TapLinkAppConstants.TAP_LINK_SYS;

public class SecurityUtils {

    public static Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Users) {
            return (Users) authentication.getPrincipal();
        }
        return null;
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName(); // Returns the username string
        }
        return TAP_LINK_SYS;
    }
}
