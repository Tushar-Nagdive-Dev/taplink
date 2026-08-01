package org.co.taplink.links.services;

import org.co.taplink.links.modals.LinkRequest;
import org.co.taplink.links.modals.LinkResponse;
import org.co.taplink.links.modals.ReorderLinksRequest;

import java.util.List;

public interface LinkService {

    LinkResponse createLink(LinkRequest request, String username);

    List<LinkResponse> getAllLinksForUser(String username);

    LinkResponse updateLink(Long linkId, LinkRequest request, String username);

    void deleteLink(Long linkId, String username);

    LinkResponse updateFavorite(Long linkId, Boolean isFavorite, String username);

    Boolean updateStatus(Long linkId, String username, Boolean isActive);

    void updateLinkPositions(ReorderLinksRequest request, String username);
}
