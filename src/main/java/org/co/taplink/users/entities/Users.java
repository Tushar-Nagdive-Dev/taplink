package org.co.taplink.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String password;

    private Boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSession> sessions = new HashSet<>();

    public void setUserProfile(UserProfile userProfile) {
        if(userProfile == null) {
            if(this.userProfile != null) {
                this.userProfile.setUser(null);
            }
        }else {
            userProfile.setUser(this);
        }
        this.userProfile = userProfile;
    }

    public void addRole(Roles role) {
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(this);
        userRole.setActive(true);
        this.userRoles.add(userRole);
    }

    public void removeRole(Roles role) {
        this.userRoles.removeIf(userRole -> userRole.getRole().equals(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream().filter(UserRole::getActive)
                .map(UserRole::getRole)
                .map(Roles::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
