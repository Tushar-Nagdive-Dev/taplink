package org.co.taplink.links.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.configs.security.SecurityUtils;
import org.co.taplink.links.modals.LinkRequest;
import org.co.taplink.links.modals.LinkResponse;
import org.co.taplink.links.services.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.LINKS_PATH;

@Slf4j
@RestController
@RequestMapping(LINKS_PATH)
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<@NonNull LinkResponse> createLink(@RequestBody LinkRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Creating link for user {}", username);
        return new ResponseEntity<>(this.linkService.createLink(request, username), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<@NonNull List<LinkResponse>> getUserLinks() {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Get user link for user {}", username);
        return ResponseEntity.ok(this.linkService.getAllLinksForUser(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull LinkResponse> updateLink(@PathVariable Long id, @RequestBody LinkRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Updating link for user {}", username);
        return new ResponseEntity<>(this.linkService.updateLink(id, request, username), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> deleteLink(@PathVariable Long id) {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Delete link for user {}", username);
        linkService.deleteLink(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<@NonNull LinkResponse> updateFavorite(@PathVariable Long id, @RequestParam Boolean isFavorite) {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Patching favorite status for user {}", username);
        return ResponseEntity.ok(this.linkService.updateFavorite(id, isFavorite, username));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<@NonNull Boolean> updateStatus(@PathVariable Long id, @RequestParam Boolean isActive) {
        String username = SecurityUtils.getCurrentUsername();
        log.info("Patching active status for user {}", username);
        return ResponseEntity.ok(this.linkService.updateStatus(id, username, isActive));
    }
}
