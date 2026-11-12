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

    public String generateAccessToken(String username, Roles role) {
        return buildToken(username, role, accessTokenDuration);
    }

    public String generateRefreshToken(String username, Roles role) {
        return buildToken(username, role, refreshTokenDuration);
    }

    private String buildToken(String username, Roles role, long duration) {
        return Jwts.builder()
                .subject("Laundry Management System")
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duration))
                .issuer("LAUNDRY")
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String validateTokenAndRetrieveSubject(String token) throws JwtException {
        Claims claims = parse(token);

        // Kiểm tra issuer và subject để đảm bảo tính nhất quán
        if (!"LAUNDRY".equals(claims.getIssuer()) ||
                !"Laundry Management System".equals(claims.getSubject())) {
            throw new JwtException("Token không hợp lệ");
        }

        return claims.get("username", String.class);
    }

    public String extractRole(String token) {
        return parse(token).get("role", String.class);
    }

    public Date extractExpiration(String token) throws JwtException {
        return parse(token).getExpiration();
    }
}
