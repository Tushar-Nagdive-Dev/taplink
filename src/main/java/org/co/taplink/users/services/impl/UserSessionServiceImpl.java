package org.co.taplink.users.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.exception.UserSessionNotFoundException;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.repository.UserSessionRepository;
import org.co.taplink.users.services.UserSessionService;
import org.co.taplink.utils.TapLinkRequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Override
    @Transactional
    public UserSession createSession(Users user, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        UserSession session = new UserSession();
        session.setUser(user);
        session.setSessionId(UUID.randomUUID());
        session.setLoginAt(now);
        session.setLastActive(now);
        session.setExpiresAt(now.plusDays(7));
        session.setIpAddress(TapLinkRequestUtils.getClientIpAddress(request));
        session.setBrowser(TapLinkRequestUtils.getBrowser(request));
        session.setOsType(TapLinkRequestUtils.getOperatingSystem(request));
        session.setIsActive(true);
        log.info("Created Session UUID : {}", session.getSessionId());
        return userSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void deleteSessionByUser(Users user) {
        this.userSessionRepository.deleteByUserId(user.getId());
    }

    @Override
    public Optional<UserSession> findBySessionId(UUID sessionId) {
        return this.userSessionRepository.findBySessionId(sessionId);
    }

    @Override
    public void deactivateSession(UUID sessionId) {
        UserSession session = this.userSessionRepository.findBySessionId(sessionId).orElseThrow(UserSessionNotFoundException::new);
        session.setIsActive(false);
        session.setLogoutAt(LocalDateTime.now());
        this.userSessionRepository.save(session);
    }
}
