package org.co.taplink.users.services;

import org.co.taplink.users.modals.UserProfileDto;

public interface UserProfileService {
    UserProfileDto getCurrentUserProfile();

    UserProfileDto updateCurrentUserProfile(UserProfileDto request);
}
