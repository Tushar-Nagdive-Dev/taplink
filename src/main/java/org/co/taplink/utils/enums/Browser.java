package org.co.taplink.utils.enums;

import lombok.Getter;

import java.util.Locale;

public enum Browser {

    EDGE("edg", "Edge"),
    OPERA("opr", "Opera"),
    FIREFOX("firefox", "Firefox"),
    CHROME("chrome", "Chrome"),
    SAFARI("safari", "Safari"),
    UNKNOWN("", "Unknown Browser");

    private final String value;

    @Getter
    private final String name;

    Browser(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Browser fromUserAgent(String agent) {
        if (agent == null || agent.isEmpty()) {
            return UNKNOWN;
        }

        String userAgent = agent.toLowerCase(Locale.ROOT);
        for (Browser browser : values()) {
            if(browser == UNKNOWN) {
                continue;
            }

            if(!userAgent.contains(browser.value)) {
                continue;
            }

            if (browser == SAFARI && userAgent.contains(CHROME.value)) {
                continue;
            }

            // Edge UA also contains Chrome
            if (browser == CHROME && userAgent.contains(EDGE.value)) {
                continue;
            }

            // Opera UA also contains Chrome
            if (browser == CHROME && userAgent.contains(OPERA.value)) {
                continue;
            }
            return browser;
        }
        return UNKNOWN;
    }
}
