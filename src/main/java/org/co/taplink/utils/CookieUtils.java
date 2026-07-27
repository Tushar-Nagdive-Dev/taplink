package org.co.taplink.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CookieUtils {
    private CookieUtils() {
        throw new AssertionError("cannot instantiate utility class");
    }

    public static String getCookiesValue(HttpServletRequest request, String cookieName) {
        if(request.getCookies() == null) {
            log.info("Cookies : NONE");
            return null;
        }else {
            log.info("Cookies :");
            for (Cookie cookie : request.getCookies()) {
                log.info("  {}={}", cookie.getName(), cookie.getValue());
            }
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
