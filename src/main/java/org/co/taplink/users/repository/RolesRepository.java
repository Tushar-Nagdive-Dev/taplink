package org.co.taplink.users.repository;

import lombok.NonNull;
import org.co.taplink.users.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<@NonNull Roles,@NonNull Long> {
    Optional<Roles> findByName(String name);
}
