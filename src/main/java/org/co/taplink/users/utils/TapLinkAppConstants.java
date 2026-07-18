package org.co.taplink.users.utils;

public final class TapLinkAppConstants {

    private TapLinkAppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static final String ROLE_USER = "ROLE_USER";
    public static final String BEARER = "Bearer";
    public static final String AUTHORIZED = "Authorized";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INTERNAL_SERVER = "Internal Server Error";
}
