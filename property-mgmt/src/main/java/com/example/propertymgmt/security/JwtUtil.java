package com.example.propertymgmt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Handles creating and reading JWTs (JSON Web Tokens).
 *
 * How this fits together: when a user logs in successfully, we generate a
 * signed token containing their email and an expiry date. The frontend
 * stores this token and sends it back on every future request in the
 * "Authorization: Bearer <token>" header. JwtAuthFilter (below) reads and
 * verifies that token on each request - no server-side session needed,
 * which is why this approach is called "stateless" auth.
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secretString;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        // The secret must be at least 256 bits (32 bytes) for HS256 - see application.properties
        return Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            // covers expired, malformed, or tampered tokens
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
