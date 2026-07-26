package org.co.taplink.utils;

public final class TapLinkAppMessages {
    private TapLinkAppMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static final class Auth {
        // %s expects a String (e.g., the actual username they tried to use)
        public static final String USERNAME_TAKEN = "Registration failed: Username '%s' is already taken!";
        public static final String EMAIL_TAKEN = "Registration failed: Email '%s' is already registered!";
        public static final String ROLE_NOT_FOUND = "Critical Error: System role '%s' not found in the database.";
        public static final String INVALID_USER = "Authentication failed for user: '%s'.";
        public static final String SESSION_EXPIRED = "Session expired. Please login again.";

        // Static messages remain normal
        public static final String LOGIN_SUCCESS = "Successfully logged in.";
        public static final String REGISTER_SUCCESS = "Registration successful.";
        public static final String DEFAULT_ROLE_NOT_FOUND = "Default Role not found in database.";
        public static final String SUCCESSFUL_LOGOUT = "Successfully logged out and token destroyed";
        public static final String USER_PROFILE_NOT_FOUND = "User Profile not found";
    }

    public static final class Error {
        public static final String UNAUTHORIZED_MSG = "Invalid username or password.";
        public static final String FORBIDDEN_MSG = "You do not have permission to access resource: '%s'.";
        public static final String INTERNAL_SERVER_MSG = "An unexpected error occurred. Please try again later.";
        public static final String TOKEN_EXPIRED = "Your session has expired. Please log in again.";
    }

    public static final class Link {
        // %d expects a whole number like Long or Integer (e.g., the Link ID)
        public static final String NOT_FOUND = "The requested link with ID %d could not be found.";
        public static final String CREATED = "Link for URL '%s' saved successfully.";
        public static final String DELETED = "Link with ID %d was deleted successfully.";
        public static final String SUSPENDED = "Link with ID %d has been temporarily suspended.";
        public static final String USER_NOT_FOUND = "The User with username %s could not be found.";
    }

    public static final class Common {
        public static final String INTERACTIVE_API = "Interactive API testing interface for the Taplink platform.";
    }

    public static final class QrBarcodes {
        public static final String QA_BARCODE_DISABLED = "QR/Barcode generation is disabled for this link.";
        public static final String FAILED_QA_BARCODE_GENERATION = "Failed to generate code image for URL: {}";
        public static final String FAILED_LOGO_LOAD = "Failed to load logo from URL: {}. Returning base QR code.";
        public static final String LINK_NOT_FOUND = "Link not found with ID: ";
    }
}
