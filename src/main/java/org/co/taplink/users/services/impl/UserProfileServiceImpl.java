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
import org.springframework.transaction.annotation.Transactional;

import static org.co.taplink.utils.TapLinkAppMessages.Auth.USER_PROFILE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UsersRepository usersRepository;

    @Override
    public UserProfileDto getCurrentUserProfile() {
        Users user = getUser();
        UserProfile profile = user.getUserProfile();
        return new UserProfileDto(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getProfilePictureUrl(),
                profile.getBio(),
                profile.getLocation(),
                profile.getTimezone());
    }

    @Override
    @Transactional
    public UserProfileDto updateCurrentUserProfile(UserProfileDto request) {
        Users currentUser = getUser();
        UserProfile profile = currentUser.getUserProfile();
        if(profile != null) {
            profile.setFirstName(request.firstName());
            profile.setLastName(request.lastName());
            profile.setProfilePictureUrl(request.profilePictureUrl());
            profile.setBio(request.bio());
            profile.setLocation(request.location());
            profile.setTimezone(request.timezone());
            this.userProfileRepository.save(profile);
            return request;
        } else {
            throw new RuntimeException(USER_PROFILE_NOT_FOUND);
        }
    }

    private static Users getUser() {
        Users user = SecurityUtils.getCurrentUser();
        if(user != null) {
            log.info("getCurrentUserProfile() called by username: {}", user.getUsername());
        } else {
            throw new IllegalStateException("Current user is null");
        }
        return user;
    }

}
