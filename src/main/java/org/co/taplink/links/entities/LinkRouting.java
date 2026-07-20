package org.co.taplink.links.entities;

import jakarta.persistence.*;
import lombok.*;
import org.co.taplink.configs.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link_routing")
public class LinkRouting extends BaseEntity {

    @Id
    private Long id;

    @MapsId
    @JoinColumn(name = "user_links_id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private UserLinks userLinks;

    @Column(name = "short_code", length = 50, unique = true)
    private String shortCode;

    @Column(name = "custom_slug", length = 50, unique = true)
    private String customSlug;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
