package org.co.taplink.utils;

public final class TapLinkAppMessages {
    private TapLinkAppMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static final class DEV_PORTAL {
        public static final String HTML_CONTENT = """
                            <!DOCTYPE html>
                            <html lang="en">
                            <head>
                                <meta charset="UTF-8">
                                <title>Taplink Local Development Portal</title>
                                <style>
                                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #0f172a; color: #f8fafc; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
                                    .card { background: #1e293b; padding: 40px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.3); text-align: center; max-width: 500px; border: 1px solid #334155; }
                                    h1 { color: #38bdf8; margin-top: 0; font-size: 24px; }
                                    p { color: #94a3b8; line-height: 1.6; }
                                    .btn { display: inline-block; margin-top: 20px; background: #0284c7; color: white; padding: 12px 24px; border-radius: 6px; text-decoration: none; font-weight: 600; transition: background 0.2s; }
                                    .btn:hover { background: #0369a1; }
                                    .badge { background: #065f46; color: #34d399; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
                                </style>
                            </head>
                            <body>
                                <div class="card">
                                    <span class="badge">PURE DEV MODE</span>
                                    <h1>Taplink Backend API Server</h1>
                                    <p>You are accessing the backend server directly on port <strong>1005</strong>.</p>
                                    <p>Direct UI access is disabled during local development. Please use the Angular development server instead.</p>
                                    <a href="http://localhost:4200" class="btn">🚀 Open Angular Dev Server (Port 4200)</a>
                                </div>
                            </body>
                            </html>""";
    }

    public static final class Auth {
        // %s expects a String (e.g., the actual username they tried to use)
        public static final String USERNAME_TAKEN = "Registration failed: Username '%s' is already taken!";
        public static final String EMAIL_TAKEN = "Registration failed: Email '%s' is already registered!";
        public static final String ROLE_NOT_FOUND = "Critical Error: System role '%s' not found in the database.";
        public static final String INVALID_USER = "Authentication failed for user: '%s'.";
        public static final String SESSION_EXPIRED = "Session expired. Please login again.";
        public static final String USER_SESSION_NOT_FOUND = "User's session not found";

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
