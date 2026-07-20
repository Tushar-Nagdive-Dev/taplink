package org.co.taplink.links.entities;

import jakarta.persistence.*;
import lombok.*;
import org.co.taplink.configs.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link_presentation")
public class LinkPresentation extends BaseEntity {

    @Id
    private Long id;

    @MapsId
    @JoinColumn(name = "user_links_id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private UserLinks userLinks;

    @Column(length = 500)
    private String label;

    @Builder.Default
    @Column(name = "color_code", length = 7)
    private String colorCode = "#FFFFFF";

    @Builder.Default
    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

}
