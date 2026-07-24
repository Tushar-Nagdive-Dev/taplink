package org.co.taplink.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.co.taplink.configs.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio", length = 250)
    private String bio;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "auth_provider", length = 20)
    private String authProvider;

}
