package org.co.taplink.users.services;

import jakarta.servlet.http.HttpServletRequest;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.entities.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionService {
    UserSession createSession(Users user, HttpServletRequest request);

    void deleteSessionByUser(Users user);

    Optional<UserSession> findBySessionId(UUID sessionId);

    void deactivateSession(UUID sessionId);
}
