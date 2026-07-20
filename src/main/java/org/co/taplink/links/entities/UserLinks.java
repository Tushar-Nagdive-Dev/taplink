package org.co.taplink.links.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;
import org.co.taplink.users.entities.Users;

@Getter
@Setter
@Entity
@Table(name = "user_links")
public class UserLinks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2048)
    private String url;

    // Determines the display order on the user's public page
    @Column(nullable = false)
    private Integer position = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
