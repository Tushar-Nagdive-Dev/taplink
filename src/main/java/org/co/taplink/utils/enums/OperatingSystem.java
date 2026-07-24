package org.co.taplink.utils.enums;

import lombok.Getter;

import java.util.Locale;

public enum OperatingSystem {
    ANDROID("android", "Android"),
    IOS_IPHONE("iphone", "iOS"),
    IOS_IPAD("ipad", "iOS"),
    WINDOWS("windows", "Windows"),
    MAC("mac", "macOS"),
    LINUX("linux", "Linux"),
    UNKNOWN("", "Unknown OS");

    private final String value;

    @Getter
    private final String name;

    OperatingSystem(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static OperatingSystem fromUserAgent(String agent) {
        if (agent == null || agent.isEmpty()) {
            return UNKNOWN;
        }

        String userAgent = agent.toLowerCase(Locale.ROOT);
        for (OperatingSystem os : values()) {
            if (os == UNKNOWN) {
                continue;
            }

            if(userAgent.contains(os.value)) {
                return os;
            }
        }
        return UNKNOWN;
    }

}
