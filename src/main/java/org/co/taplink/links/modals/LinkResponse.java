package org.co.taplink.links.modals;

public record LinkResponse(
        Long id,
        String title,
        String url,
        Integer position,
        Boolean isActive,
        String createdAt
) {}
