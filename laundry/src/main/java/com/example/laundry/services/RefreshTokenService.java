package com.example.laundry.services;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;

public interface RefreshTokenService {
  RefreshToken createRefreshToken(User user);
  RefreshToken verifyExpiration(RefreshToken token);
  void deleteByUser(User user);
  RefreshToken findByToken(String token);
  void deleteByToken(String token);
}
