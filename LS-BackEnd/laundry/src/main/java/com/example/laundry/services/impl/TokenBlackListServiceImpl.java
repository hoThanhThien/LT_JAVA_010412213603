package com.example.laundry.services.impl;

import com.example.laundry.models.notification.BlacklistedToken;
import com.example.laundry.repository.BlacklistedTokenRepository;
import com.example.laundry.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

/**
 * Blacklist access token lưu trong DB: không mất khi restart, an toàn khi nhiều luồng,
 * và được dọn định kỳ (token hết hạn thì tự vô hiệu nên không cần giữ nữa).
 */
@Service
public class TokenBlackListServiceImpl {
  private static final Logger log = LoggerFactory.getLogger(TokenBlackListServiceImpl.class);

  private final BlacklistedTokenRepository repository;
  private final JwtUtil jwtUtil;

  public TokenBlackListServiceImpl(BlacklistedTokenRepository repository, JwtUtil jwtUtil) {
    this.repository = repository;
    this.jwtUtil = jwtUtil;
  }

  @Transactional
  public void addBlacklistToken(String token) {
    Instant expiresAt;
    try {
      expiresAt = jwtUtil.extractExpiration(token).toInstant();
    } catch (JwtException | IllegalArgumentException e) {
      // Token sai chữ ký hoặc đã hết hạn thì filter đã từ chối sẵn, không cần lưu
      log.debug("Bỏ qua blacklist cho token không hợp lệ: {}", e.getMessage());
      return;
    }
    repository.save(new BlacklistedToken(hash(token), expiresAt));
  }

  @Transactional(readOnly = true)
  public boolean isBlacklisted(String token) {
    return repository.existsById(hash(token));
  }

  /** Dọn các token đã hết hạn, mỗi giờ một lần. */
  @Scheduled(fixedRate = 3_600_000, initialDelay = 60_000)
  @Transactional
  public void purgeExpired() {
    long removed = repository.deleteByExpiresAtBefore(Instant.now());
    if (removed > 0) {
      log.info("Đã dọn {} token blacklist hết hạn", removed);
    }
  }

  private static String hash(String token) {
    try {
      byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
