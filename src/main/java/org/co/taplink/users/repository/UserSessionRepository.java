package org.co.taplink.users.repository;

import lombok.NonNull;
import org.co.taplink.users.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<@NonNull UserSession,@NonNull Long> {
    List<UserSession> findByUserIdAndIsActiveTrue(@NonNull Long userId);
}
