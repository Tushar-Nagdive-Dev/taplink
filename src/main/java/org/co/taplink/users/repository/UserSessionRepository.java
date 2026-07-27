package org.co.taplink.users.repository;

import lombok.NonNull;
import org.co.taplink.users.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<@NonNull UserSession,@NonNull Long> {
    Optional<UserSession> findBySessionId(@NonNull UUID sessionId);
    void deleteByUserId(Long userId);
}
