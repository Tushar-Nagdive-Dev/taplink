package org.co.taplink.users.repository;

import lombok.NonNull;
import org.co.taplink.users.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<@NonNull UserProfile,@NonNull Long> {

    @NonNull
    Optional<UserProfile> findById(Long id);
}
