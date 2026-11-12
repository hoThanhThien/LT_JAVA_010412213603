package com.example.laundry.security;

import com.example.laundry.models.user.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey key;

    // Hạn token (ms) lấy từ cấu hình: jwt.access.duration / jwt.refresh.duration
    // jwt.refresh.duration cũng là giá trị dùng cho cột expiresAt của bảng refresh_tokens
    private final long accessTokenDuration;
    private final long refreshTokenDuration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access.duration}") long accessTokenDuration,
                   @Value("${jwt.refresh.duration}") long refreshTokenDuration) {
        // JJWT yêu cầu một SecretKey thay vì một String
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenDuration = accessTokenDuration;
        this.refreshTokenDuration = refreshTokenDuration;
    }

    public String generateAccessToken(UUID userId, String username, Roles role) {
        return buildToken(userId, username, role, accessTokenDuration);
    }

    public String generateAccessToken(String username, Roles role) {
        return buildToken(null, username, role, accessTokenDuration);
    }

    public String generateRefreshToken(UUID userId, String username, Roles role) {
        return buildToken(userId, username, role, refreshTokenDuration);
    }

    public String generateRefreshToken(String username, Roles role) {
        return buildToken(null, username, role, refreshTokenDuration);
    }

    private String buildToken(UUID userId, String username, Roles role, long duration) {
        var builder = Jwts.builder()
                .subject("Laundry Management System")
                .claim("username", username)
                .claim("role", role != null ? role.name() : null)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration))
                .issuer("LAUNDRY")
                .signWith(key, Jwts.SIG.HS256);

        if (userId != null) {
            builder.claim("userId", userId.toString());
        }

        return builder.compact();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims parseAndValidate(String token) throws JwtException {
        Claims claims = parse(token);
        if (!"LAUNDRY".equals(claims.getIssuer()) ||
                !"Laundry Management System".equals(claims.getSubject())) {
            throw new JwtException("Token không hợp lệ");
        }
        return claims;
    }

    /**
     * Validates the token and returns the immutable userId claim when present,
     * falling back to the legacy username claim for pre-existing tokens.
     */
    public String validateTokenAndRetrieveSubject(String token) throws JwtException {
        Claims claims = parseAndValidate(token);
        String userId = claims.get("userId", String.class);
        if (userId != null && !userId.isBlank()) {
            return userId;
        }
        String username = claims.get("username", String.class);
        if (username == null || username.isBlank()) {
            throw new JwtException("Token thiếu định danh người dùng");
        }
        return username;
    }

    public Optional<UUID> extractUserId(String token) {
        return SecurityUtils.parseUuid(parse(token).get("userId", String.class));
    }

    public String extractRole(String token) {
        return parse(token).get("role", String.class);
    }

    public Date extractExpiration(String token) throws JwtException {
        return parse(token).getExpiration();
    }
}
