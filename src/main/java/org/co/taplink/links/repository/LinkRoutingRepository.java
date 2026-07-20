package org.co.taplink.links.repository;

import lombok.NonNull;
import org.co.taplink.links.entities.LinkRouting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRoutingRepository extends JpaRepository<@NonNull LinkRouting,@NonNull Long> {

    Boolean existsByShortCode(String shortCode);

    Optional<LinkRouting> findByShortCode(String shortCode);
}
