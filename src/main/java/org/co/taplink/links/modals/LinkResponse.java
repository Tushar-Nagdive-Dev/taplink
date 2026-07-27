package org.co.taplink.links.modals;

import java.time.LocalDateTime;

public record LinkResponse(
        Long id,
        String title,
        String url,
        Integer position,
        Boolean isActive,
        String shortCode,
        String customSlug,
        LocalDateTime expiresAt,
        Boolean isFavorite,
        String colorCode,
        String label,
        String createdAt
) {}