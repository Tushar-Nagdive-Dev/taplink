package org.co.taplink.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_session")
public class UserSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "session_id", nullable = false, unique = true, updatable = false)
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

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
