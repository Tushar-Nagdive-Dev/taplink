package org.co.taplink.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;

@Entity
@Table(name = "user_roles",  uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_role", columnNames = {
                "user_id", "role_id"
        })
})
@Setter
@Getter
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @Column(nullable = false)
    private Boolean active = true;
}
