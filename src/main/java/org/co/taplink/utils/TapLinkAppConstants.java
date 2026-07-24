package org.co.taplink.utils;

public final class TapLinkAppConstants {

    private TapLinkAppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static final String MATCH_ALL = "**";
    public static final String FORWARD_SLASH = "/";

    public static final class API_PATHS {
        public static final String AUTH_PATH = "/api/v1/auth";
        public static final String SWAGGER_UI_PATH = "/swagger-ui";
        public static final String ADMIN_PATH = "/api/v1/admin";
        public static final String PREMIUM_USER_PATH = "/api/v1/premium";
        public static final String SWAGGER_HTML_PATH = "/swagger-ui.html";
        public static final String V3_API_DOCS_PATH = "/v3/api-docs/**";
        public static final String LINKS_PATH = "/api/v1/links";
        public static final String QR_BARCODE_PATH = "/api/v1/qrcode";
    }

    public static final class ROLES {
        public static final String ADMIN = "ADMIN";
        public static final String PREMIUM = "PREMIUM";
        public static final String USER = "USER";
    }

    public static final class REQUEST_HEADERS {
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
        public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
        public static final String CF_CONNECTING_IP = "CF-Connecting-IP";
        public static final String X_REAL_IP = "X-Real-IP";
    }

    public static final String[] CLIENT_IP_HEADERS = {
            REQUEST_HEADERS.X_FORWARDED_FOR,
            REQUEST_HEADERS.PROXY_CLIENT_IP,
            REQUEST_HEADERS.WL_PROXY_CLIENT_IP,
            REQUEST_HEADERS.HTTP_CLIENT_IP,
            REQUEST_HEADERS.HTTP_X_FORWARDED_FOR,
            REQUEST_HEADERS.CF_CONNECTING_IP,
            REQUEST_HEADERS.X_REAL_IP
    };


    public static final String USER_AGENT = "User-Agent";
    public static final String UNKNOWN = "unknown";
    public static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int DEFAULT_CODE_LENGTH = 6;
    public static final String ROLE_USER = "ROLE_USER";
    public static final String BEARER = "Bearer";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INTERNAL_SERVER = "Internal Server Error";
    public static final String TAP_LINK_SYS = "Tap Link System";
    public static final String TAPLINK_API_DOCUMENTATION = "Taplink API Documentation";
    public static final String VER_1_0 = "1.0";
    public static final String JWT = "JWT";
    public static final String BEARER_AUTH = "bearerAuth";
}
