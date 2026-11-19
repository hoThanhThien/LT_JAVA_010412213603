package com.example.laundry.services.impl;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.RefreshTokenRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private static final int MAX_ACTIVE_SESSIONS_PER_USER = 5;

  @Value("${jwt.refresh.duration}")
  private Long refreshTokenDuration;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Transactional
  @Override
  public RefreshToken createRefreshToken(User user) {
    Instant now = Instant.now();
    refreshTokenRepository.deleteByUserAndExpiresAtBefore(user, now);

    List<RefreshToken> activeTokens = refreshTokenRepository.findByUserOrderByExpiresAtDesc(user);
    if (activeTokens.size() >= MAX_ACTIVE_SESSIONS_PER_USER) {
      List<RefreshToken> toRemove = activeTokens.subList(MAX_ACTIVE_SESSIONS_PER_USER - 1, activeTokens.size());
      refreshTokenRepository.deleteAllInBatch(toRemove);
    }

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(jwtUtil.generateRefreshToken(user.getId(), user.getUsername(), user.getRoles()));
    refreshToken.setExpiresAt(now.plusMillis(refreshTokenDuration));
    refreshToken.setCreatedAt(now);
    refreshToken.setDeviceInfo(resolveDeviceInfo(null));
    refreshToken.setIpAddress(resolveClientIp(null));
    return refreshTokenRepository.save(refreshToken);
  }

  @Transactional
  @Override
  public RefreshToken rotateRefreshToken(String rawRefreshToken, String deviceInfo, String ipAddress) {
    jwtUtil.validateRefreshToken(rawRefreshToken);

    RefreshToken existing = findByToken(rawRefreshToken);
    verifyExpiration(existing);

    User user = existing.getUser();
    String resolvedDevice = resolveDeviceInfo(
            (deviceInfo != null && !deviceInfo.isBlank()) ? deviceInfo : existing.getDeviceInfo()
    );
    String resolvedIp = resolveClientIp(
            (ipAddress != null && !ipAddress.isBlank()) ? ipAddress : existing.getIpAddress()
    );

    int deleted = refreshTokenRepository.deleteByTokenReturningCount(rawRefreshToken);
    refreshTokenRepository.flush();
    if (deleted == 0) {
      throw new RuntimeException("Refresh token was already consumed");
    }

    Instant now = Instant.now();
    RefreshToken rotated = new RefreshToken();
    rotated.setUser(user);
    rotated.setToken(jwtUtil.generateRefreshToken(user.getId(), user.getUsername(), user.getRoles()));
    rotated.setExpiresAt(now.plusMillis(refreshTokenDuration));
    rotated.setCreatedAt(now);
    rotated.setDeviceInfo(resolvedDevice);
    rotated.setIpAddress(resolvedIp);
    return refreshTokenRepository.save(rotated);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RuntimeException("Refresh token was expired. Please login again.");
    }
    return token;
  }

  @Override
  @Transactional
  public void deleteByUser(User user) {
    refreshTokenRepository.deleteByUser(user);
  }

  @Override
  public RefreshToken findByToken(String token) {
    return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));
  }

  @Override
  public void deleteByToken(String token) {
    refreshTokenRepository.deleteByToken(token);
  }

  @Override
  @Transactional
  public List<Map<String, Object>> getActiveSessions(User user) {
    Instant now = Instant.now();
    refreshTokenRepository.deleteByUserAndExpiresAtBefore(user, now);

    List<RefreshToken> sessions = refreshTokenRepository.findByUserOrderByExpiresAtDesc(user);
    List<Map<String, Object>> result = new ArrayList<>();
    for (RefreshToken session : sessions) {
      if (session.getExpiresAt() != null && session.getExpiresAt().isAfter(now)) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", session.getId());
        item.put("deviceInfo", session.getDeviceInfo() != null ? session.getDeviceInfo() : "Unknown device");
        item.put("ipAddress", session.getIpAddress() != null ? session.getIpAddress() : "local");
        item.put("createdAt", session.getCreatedAt());
        item.put("expiresAt", session.getExpiresAt());
        result.add(item);
      }
    }
    return result;
  }

  @Override
  @Transactional
  public boolean revokeSession(User user, Long sessionId) {
    Optional<RefreshToken> sessionOpt = refreshTokenRepository.findByIdAndUser(sessionId, user);
    if (sessionOpt.isEmpty()) {
      return false;
    }
    refreshTokenRepository.delete(sessionOpt.get());
    return true;
  }

  private String resolveDeviceInfo(String fallback) {
    HttpServletRequest request = currentHttpRequest();
    if (request != null) {
      String ua = request.getHeader("User-Agent");
      if (ua != null && !ua.isBlank()) {
        return ua.length() > 250 ? ua.substring(0, 250) : ua;
      }
    }
    if (fallback != null && !fallback.isBlank()) {
      return fallback.length() > 250 ? fallback.substring(0, 250) : fallback;
    }
    return "Unknown device";
  }

  private String resolveClientIp(String fallback) {
    HttpServletRequest request = currentHttpRequest();
    if (request != null) {
      String forwardedFor = request.getHeader("X-Forwarded-For");
      if (forwardedFor != null && !forwardedFor.isBlank()) {
        String firstIp = forwardedFor.split(",")[0].trim();
        return firstIp.length() > 64 ? firstIp.substring(0, 64) : firstIp;
      }
      String remoteAddr = request.getRemoteAddr();
      if (remoteAddr != null && !remoteAddr.isBlank()) {
        return remoteAddr.length() > 64 ? remoteAddr.substring(0, 64) : remoteAddr;
      }
    }
    return (fallback != null && !fallback.isBlank()) ? fallback : "local";
  }

  private HttpServletRequest currentHttpRequest() {
    var attrs = RequestContextHolder.getRequestAttributes();
    if (attrs instanceof ServletRequestAttributes servletAttrs) {
      return servletAttrs.getRequest();
    }
    return null;
  }
}
