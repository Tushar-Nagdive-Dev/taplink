package org.co.taplink.links.services;

import org.co.taplink.links.modals.LinkRequest;
import org.co.taplink.links.modals.LinkResponse;

import java.util.List;

public interface LinkService {

    LinkResponse createLink(LinkRequest request, String username);

    List<LinkResponse> getAllLinksForUser(String username);

    LinkResponse updateLink(Long linkId, LinkRequest request, String username);

    void deleteLink(Long linkId, String username);
}
