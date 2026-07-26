package org.co.taplink.users.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.security.SecurityUtils;
import org.co.taplink.users.entities.UserProfile;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.modals.UserProfileDto;
import org.co.taplink.users.repository.UserProfileRepository;
import org.co.taplink.users.repository.UsersRepository;
import org.co.taplink.users.services.UserProfileService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public final class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UsersRepository usersRepository;

    @Override
    public UserProfileDto getCurrentUserProfile() {
        Users user = SecurityUtils.getCurrentUser();
        if(user != null) {
            log.info("getCurrentUserProfile() called by username: {}", user.getUsername());
        } else {
            throw new IllegalStateException("getCurrentUserProfile() called by username: null");
        }
        UserProfile profile = user.getUserProfile();
        return new UserProfileDto(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getProfilePictureUrl(),
                profile.getBio(),
                profile.getLocation(),
                profile.getLocation());
    }
}
