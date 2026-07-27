package org.co.taplink.configs.security;

import lombok.extern.slf4j.Slf4j;
import org.co.taplink.users.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.co.taplink.utils.TapLinkAppConstants.TAP_LINK_SYS;

@Slf4j
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Users getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("========== SecurityUtils.getCurrentUser() ==========");
        log.info("Authentication Object : {}", authentication);

        if (authentication == null) {
            log.warn("Authentication is NULL");
            return null;
        }

        log.info("Authentication Type   : {}", authentication.getClass().getName());
        log.info("Authenticated         : {}", authentication.isAuthenticated());

        Object principal = authentication.getPrincipal();

        if (principal == null) {
            log.warn("Principal is NULL");
            return null;
        }

        log.info("Principal Type        : {}", principal.getClass().getName());
        log.info("Principal Value       : {}", principal);

        if (principal instanceof Users user) {
            log.info("Returning Users object");
            return user;
        }

        log.warn("Principal is NOT Users.");
        log.warn("Principal = {}", principal);

        return null;
    }

    public static String getCurrentUsername() {

        Users user = getCurrentUser();

        if (user != null) {
            return user.getUsername();
        }

        return TAP_LINK_SYS;
    }

    public static Long getCurrentUserId() {

        Users user = getCurrentUser();

        if (user != null) {
            return user.getId();
        }

        return null;
    }

    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}