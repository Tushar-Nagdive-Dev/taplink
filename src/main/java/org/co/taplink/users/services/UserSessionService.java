package org.co.taplink.users.services;

import jakarta.servlet.http.HttpServletRequest;
import org.co.taplink.users.entities.Users;

public interface UserSessionService {
    void createSession(Users user, HttpServletRequest request);
}
