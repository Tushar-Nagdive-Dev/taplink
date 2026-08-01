package org.co.taplink.links.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.BaseEntity;
import org.co.taplink.links.entities.LinkPresentation;
import org.co.taplink.links.entities.LinkRouting;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.links.modals.LinkRequest;
import org.co.taplink.links.modals.LinkResponse;
import org.co.taplink.links.repository.LinkPresentationRepository;
import org.co.taplink.links.repository.LinkRoutingRepository;
import org.co.taplink.links.repository.UserLinkRepository;
import org.co.taplink.links.services.LinkService;
import org.co.taplink.links.services.ShortCodeGeneratorService;
import org.co.taplink.users.entities.Users;
import org.co.taplink.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.co.taplink.utils.TapLinkAppConstants.UNKNOWN;
import static org.co.taplink.utils.TapLinkAppMessages.Link.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final UserLinkRepository userLinkRepository;
    private final UsersRepository userRepository;

    private final LinkRoutingRepository linkRoutingRepository;
    private final LinkPresentationRepository linkPresentationRepository;
    private final ShortCodeGeneratorService shortCodeGeneratorService;

    @Override
    @Transactional
    public LinkResponse createLink(LinkRequest request, String username) {
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));

        // 1. Build and Save the Core Link
        UserLinks userLinks = new UserLinks();
        userLinks.setTitle(request.title());
        userLinks.setUrl(request.url());
        userLinks.setIsActive(request.isActive());
        userLinks.setUser(user);

        // Auto-assign position to place the new link at the bottom of their list
        List<UserLinks> existingUserLinks = this.userLinkRepository.findAllByUserIdOrderByPositionAsc(user.getId());
        userLinks.setPosition(existingUserLinks.size());

        // Save first so it generates the ID needed for the 1:1 @MapsId relations
        UserLinks savedLink = this.userLinkRepository.save(userLinks);

        // 2. Generate and Save Link Routing (Short Code)
        String uniqueShortCode = shortCodeGeneratorService.generateUniqueShortCode();
        LinkRouting routing = LinkRouting.builder()
                .userLinks(savedLink)
                .shortCode(uniqueShortCode)
                .build();
        this.linkRoutingRepository.save(routing);

        // 3. Generate and Save Link Presentation (Dashboard Defaults)
        LinkPresentation presentation = LinkPresentation.builder()
                .userLinks(savedLink)
                .label(request.title()) // Defaulting the internal label to the public title
                .colorCode("#FFFFFF")
                .isFavorite(false)
                .build();
        this.linkPresentationRepository.save(presentation);

        log.debug(String.format(CREATED, request.url()));
        return mapToResponse(savedLink);
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

        // 1. Update Core UserLink
        UserLinks userLink = this.userLinkRepository.findByIdAndUserId(linkId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND, linkId)));

        if (request.title() != null) userLink.setTitle(request.title());
        if (request.url() != null) userLink.setUrl(request.url());
        if (request.isActive() != null) userLink.setIsActive(request.isActive());

        this.userLinkRepository.save(userLink);

        // 2. Update Routing Data
        this.linkRoutingRepository.findById(linkId).ifPresent(routing -> {
            if (request.customSlug() != null) routing.setCustomSlug(request.customSlug());
            if (request.expiresAt() != null) routing.setExpiresAt(request.expiresAt());
            this.linkRoutingRepository.save(routing);
        });

        // 3. Update Presentation Data
        this.linkPresentationRepository.findById(linkId).ifPresent(presentation -> {
            if (request.label() != null) presentation.setLabel(request.label());
            if (request.colorCode() != null) presentation.setColorCode(request.colorCode());
            if (request.isFavorite() != null) presentation.setIsFavorite(request.isFavorite());
            this.linkPresentationRepository.save(presentation);
        });

        return mapToResponse(userLink);
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

    @Override
    @Transactional
    public LinkResponse updateFavorite(Long linkId, Boolean isFavorite, String username) {
        log.info("Patching favorite status for link with ID {} to favorite {}", linkId, isFavorite);
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        UserLinks userLinks = this.userLinkRepository.findByIdAndUserId(linkId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND, linkId)));
        this.linkPresentationRepository.findById(linkId).ifPresent(presentation -> {
            presentation.setIsFavorite(isFavorite);
            this.linkPresentationRepository.save(presentation);
        });
        return mapToResponse(userLinks);
    }

    @Override
    @Transactional
    public Boolean updateStatus(Long linkId, String username, Boolean isActive) {
        log.info("Active or Inactive status for link with ID {} isActive {}", linkId,  isActive);
        Users user = this.userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, username)));
        UserLinks userLinks = this.userLinkRepository.findByIdAndUserId(linkId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND, linkId)));
        userLinks.setIsActive(isActive);
        this.userLinkRepository.save(userLinks);
        return isActive;
    }

    // Updated to pull from all three repositories securely
    private LinkResponse mapToResponse(UserLinks link) {
        String createdAtStr = link.getCreatedAt() != null ?
                link.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : UNKNOWN;

        // Safely fetch related entities (they share the exact same ID due to @MapsId)
        LinkRouting routing = this.linkRoutingRepository.findById(link.getId()).orElse(null);
        LinkPresentation presentation = this.linkPresentationRepository.findById(link.getId()).orElse(null);

        return new LinkResponse(
                link.getId(),
                link.getTitle(),
                link.getUrl(),
                link.getPosition(),
                link.getIsActive(),
                routing != null ? routing.getShortCode() : null,
                routing != null ? routing.getCustomSlug() : null,
                routing != null ? routing.getExpiresAt() : null,
                presentation != null ? presentation.getIsFavorite() : false,
                presentation != null ? presentation.getColorCode() : "#FFFFFF",
                presentation != null ? presentation.getLabel() : link.getTitle(),
                createdAtStr
        );
    }
}
