package org.co.taplink.configs.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.co.taplink.users.entities.UserSession;
import org.co.taplink.users.entities.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_SESSION_ID = "sid";

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Generates a signed JWT for the authenticated user.
     */
    public String generateToken(Users user, UserSession session) {

        Map<String, Object> claims = new HashMap<>(3);

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put(CLAIM_USER_ID, user.getId());
        claims.put(CLAIM_ROLES, roles);
        claims.put(CLAIM_SESSION_ID, session.getSessionId().toString());

        return buildToken(claims, user);
    }

    /**
     * Returns the username (JWT subject).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Returns the UserSession UUID stored inside the JWT.
     */
    public UUID extractSessionId(String token) {

        String sid = extractClaim(token,
                claims -> claims.get(CLAIM_SESSION_ID, String.class));

        if (sid == null || sid.isBlank()) {
            throw new JwtException("JWT does not contain a session id.");
        }

        return UUID.fromString(sid);
    }

    /**
     * Returns the user id stored inside the JWT.
     */
    public Long extractUserId(String token) {
        return extractClaim(token,
                claims -> claims.get(CLAIM_USER_ID, Long.class));
    }

    /**
     * Extracts any claim from the JWT.
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        return claimsResolver.apply(extractAllClaims(token));
    }

    /**
     * Validates the token against the authenticated user.
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Creates the signed JWT.
     */
    private String buildToken(
            Map<String, Object> claims,
            Users user) {

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Parses and validates the JWT.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    /**
     * Returns the signing key.
     */
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}