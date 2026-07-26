package org.co.taplink.users.controllers;

import lombok.RequiredArgsConstructor;
import org.co.taplink.users.modals.UserProfileDto;
import org.co.taplink.users.services.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.USER_PROFILE_PATH;

@RestController
@RequestMapping(USER_PROFILE_PATH)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping()
    public UserProfileDto getCurrentUserProfile() {
        return this.userProfileService.getCurrentUserProfile();
    }
}
