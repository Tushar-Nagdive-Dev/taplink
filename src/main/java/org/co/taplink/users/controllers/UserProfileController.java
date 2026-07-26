package org.co.taplink.users.controllers;

import lombok.RequiredArgsConstructor;
import org.co.taplink.users.modals.UserProfileDto;
import org.co.taplink.users.services.UserProfileService;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping()
    public UserProfileDto updateCurrentUserProfile(@RequestBody UserProfileDto request) {
        return this.userProfileService.updateCurrentUserProfile(request);
    }
}
