package org.co.taplink.users.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.repository.UserSessionRepository;
import org.co.taplink.users.services.UserSessionService;
import org.co.taplink.utils.TapLinkRequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Override
    @Transactional
    public void createSession(Users user, HttpServletRequest request) {
        UserSession session = new UserSession();
        session.setUser(user);
        session.setIpAddress(TapLinkRequestUtils.getClientIpAddress(request));
        session.setBrowser(TapLinkRequestUtils.getBrowser(request));
        session.setOsType(TapLinkRequestUtils.getOperatingSystem(request));
        session.setLastActive(LocalDateTime.now());
        session.setIsActive(true);
        this.userSessionRepository.save(session);
    }
}
