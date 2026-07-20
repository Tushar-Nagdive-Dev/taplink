package org.co.taplink.links.modals;

public record LinkRequest(
        String title,
        String url,
        Boolean isActive
) {}
