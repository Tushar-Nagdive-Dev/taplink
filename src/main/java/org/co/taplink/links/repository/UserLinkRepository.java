package org.co.taplink.links.repository;

import lombok.NonNull;
import org.co.taplink.links.entities.UserLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLinkRepository extends JpaRepository<@NonNull UserLinks,@NonNull Long> {

    // Fetches all links for a specific user, perfectly sorted for the frontend
    List<UserLinks> findAllByUserIdOrderByPositionAsc(Long userId);

    // Security check: Ensures a user can only fetch/edit a link they actually own
    Optional<UserLinks> findByIdAndUserId(Long id, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE user_links SET position = :position WHERE id = :id AND user_id = :userId", nativeQuery = true)
    void updateSinglePositionForUser(
            @Param("id") Long id,
            @Param("position") Integer position,
            @Param("userId") Long userId
    );}
