package org.co.taplink.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_session")
public class UserSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "os_type", length = 50)
    private String osType;

    @Column(name = "browser", length = 50)
    private String browser;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "last_active")
    private LocalDateTime lastActive;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
