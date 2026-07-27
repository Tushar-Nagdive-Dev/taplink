package org.co.taplink.links.modals;

import java.time.LocalDateTime;

public record LinkRequest(
        String title,
        String url,
        Boolean isActive,
        String label,
        String colorCode,
        String customSlug,
        LocalDateTime expiresAt,
        Boolean isFavorite
) {}