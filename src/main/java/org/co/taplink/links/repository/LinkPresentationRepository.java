package org.co.taplink.links.repository;

import lombok.NonNull;
import org.co.taplink.links.entities.LinkPresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkPresentationRepository extends JpaRepository<@NonNull LinkPresentation,@NonNull Long> {
}
