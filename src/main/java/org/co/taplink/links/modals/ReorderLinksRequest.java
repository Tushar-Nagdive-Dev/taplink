package org.co.taplink.links.modals;

import java.util.List;

public record ReorderLinksRequest(List<LinkPosition> linkPositions) {
    public record LinkPosition(
            Long id,
            Integer position
    ){}
}
