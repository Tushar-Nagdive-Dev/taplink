package org.co.taplink.configs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.security.SecurityUtils;
import org.co.taplink.users.entities.Users;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    // --- Audit Strings ---
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", length = 50)
    private String modifiedBy;

    // --- Audit Timestamps (NEW) ---
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // --- Audit Object IDs ---
    @Column(name = "creator_id", updatable = false)
    private Long creatorId;

    @Column(name = "modifier_id")
    private Long modifierId;

    // --- Relational Mappings ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private Users creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id", insertable = false, updatable = false)
    private Users modifier;

    @PrePersist
    public void prePersist() {
        this.createdBy = SecurityUtils.getCurrentUsername();
        this.modifiedBy = SecurityUtils.getCurrentUsername();

        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            this.creatorId = currentUser.getId();
            this.modifierId = currentUser.getId();
        }
    }

    @PreUpdate
    public void preUpdate() {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            this.modifierId = currentUser.getId();
        }
    }
}
