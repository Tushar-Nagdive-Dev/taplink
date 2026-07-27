package org.co.taplink.configs.exception;

import static org.co.taplink.utils.TapLinkAppMessages.Auth.USER_SESSION_NOT_FOUND;

public class UserSessionNotFoundException extends RuntimeException{
    public UserSessionNotFoundException() {
        super(USER_SESSION_NOT_FOUND);
    }
}
