package com.example.laundry.security;

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

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // JJWT yêu cầu một SecretKey thay vì một String
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject("Laundry Management System")
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .issuer("LAUNDRY")
                .signWith(key)
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
}