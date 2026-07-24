package org.co.taplink.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.co.taplink.utils.enums.Browser;
import org.co.taplink.utils.enums.OperatingSystem;

import static org.co.taplink.utils.TapLinkAppConstants.*;

public class TapLinkRequestUtils {

    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : CLIENT_IP_HEADERS) {
            String ip = request.getHeader(header);
            if(ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    public static String getBrowser(HttpServletRequest request) {
        return Browser.fromUserAgent(getUserAgent(request)).getName();
    }

    public static String getOperatingSystem(HttpServletRequest request) {
        return OperatingSystem.fromUserAgent(getUserAgent(request)).getName();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(USER_AGENT);
    }

}
