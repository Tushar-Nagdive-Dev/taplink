package org.co.taplink.links.entities;

import jakarta.persistence.*;
import lombok.*;
import org.co.taplink.configs.BaseEntity;
import org.co.taplink.users.entities.Users;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tags extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "badge_color", length = 7)
    private String badgeColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
