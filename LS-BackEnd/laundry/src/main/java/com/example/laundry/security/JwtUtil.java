package com.example.laundry.security;

import com.example.laundry.models.user.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;

    // Token expiration time (30 minutes)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;
    private static  final long REFRESH_TOKEN_EXPIRATION = 604800000; //7 days

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // JJWT yêu cầu một SecretKey thay vì một String
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, Roles role) {
        return Jwts.builder()
                .subject("Laundry Management System")
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .issuer("LAUNDRY")
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, Roles role) {
        return Jwts.builder()
                .subject("Laundry Management System")
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .issuer("LAUNDRY")
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String validateTokenAndRetrieveSubject(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Kiểm tra issuer và subject để đảm bảo tính nhất quán
        if (!"LAUNDRY".equals(claims.getIssuer()) ||
                !"Laundry Management System".equals(claims.getSubject())) {
            throw new JwtException("Token không hợp lệ");
        }

        return claims.get("username", String.class);
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
}