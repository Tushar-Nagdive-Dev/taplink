package org.co.taplink.configs.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlocklistService {

    private final Set<String> blocklist = ConcurrentHashMap.newKeySet();

    public void blockToken(String token) {
        this.blocklist.add(token);
    }

    public boolean isBlocked(String token) {
        return this.blocklist.contains(token);
    }
}
