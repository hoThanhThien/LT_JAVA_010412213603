package com.example.laundry.services;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;

import java.util.List;
import java.util.Map;

public interface RefreshTokenService {
  RefreshToken createRefreshToken(User user);
  RefreshToken rotateRefreshToken(String rawRefreshToken, String deviceInfo, String ipAddress);
  RefreshToken verifyExpiration(RefreshToken token);
  void deleteByUser(User user);
  RefreshToken findByToken(String token);
  void deleteByToken(String token);
  List<Map<String, Object>> getActiveSessions(User user);
  boolean revokeSession(User user, Long sessionId);
}
