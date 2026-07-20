package org.co.taplink.links.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.links.modals.LinkRequest;
import org.co.taplink.links.modals.LinkResponse;
import org.co.taplink.links.repository.UserLinkRepository;
import org.co.taplink.links.services.LinkService;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.co.taplink.utils.TapLinkAppMessages.Link.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final UserLinkRepository userLinkRepository;
    private final UsersRepository userRepository;

    @Override
    @Transactional
    public LinkResponse createLink(LinkRequest request, String username) {
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        UserLinks userLinks = new UserLinks();
        userLinks.setTitle(request.title());
        userLinks.setUrl(request.url());
        userLinks.setIsActive(request.isActive());
        userLinks.setUser(user);

        // Auto-assign position to place the new link at the bottom of their list
        List<UserLinks> existingUserLinks = this.userLinkRepository.findAllByUserIdOrderByPositionAsc(user.getId());
        userLinks.setPosition(existingUserLinks.size());
        log.debug(String.format(CREATED, request.url()));
        return mapToResponse(this.userLinkRepository.save(userLinks));
    }

    @Override
    public List<LinkResponse> getAllLinksForUser(String username) {
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        return this.userLinkRepository.findAllByUserIdOrderByPositionAsc(user.getId())
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public LinkResponse updateLink(Long linkId, LinkRequest request, String username) {
        log.info("Updating link with ID {}", linkId);
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        UserLinks userLink = this.userLinkRepository.findByIdAndUserId(linkId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND, linkId)));
        userLink.setTitle(request.title());
        userLink.setUrl(request.url());
        if(request.isActive() != null) {
            userLink.setIsActive(request.isActive());
        }

        return mapToResponse(this.userLinkRepository.save(userLink));
    }

    @Override
    @Transactional
    public void deleteLink(Long linkId, String username) {
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        UserLinks userLink = this.userLinkRepository.findByIdAndUserId(linkId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND, linkId)));
        this.userLinkRepository.delete(userLink);
    }

    // Helper method to keep our controllers clean by strictly returning the DTO
    private LinkResponse mapToResponse(UserLinks link) {
        String createdAtStr = link.getCreatedAt() != null ?
                link.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "Unknown";

        return new LinkResponse(
                link.getId(),
                link.getTitle(),
                link.getUrl(),
                link.getPosition(),
                link.getIsActive(),
                createdAtStr
        );
    }
}
