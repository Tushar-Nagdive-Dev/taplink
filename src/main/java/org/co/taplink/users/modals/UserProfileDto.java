package org.co.taplink.users.modals;

public record UserProfileDto(
        String firstName,
        String lastName,
        String profilePictureUrl,
        String bio,
        String location,
        String timezone
) {}
