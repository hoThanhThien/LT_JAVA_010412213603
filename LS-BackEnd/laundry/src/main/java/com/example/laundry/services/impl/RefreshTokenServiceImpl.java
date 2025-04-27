package com.example.laundry.services.impl;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.RefreshTokenRepository;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  @Value("${jwt.refresh.duration}")
  private Long refreshTokenDuration;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Transactional
  @Override
  public RefreshToken createRefreshToken(User user) {
    //Xóa các refresh cũ của user
    refreshTokenRepository.deleteByUser(user);

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);

    String refreshTokenString = jwtUtil.generateRefreshToken(user.getUsername(), user.getRoles());
    refreshToken.setToken(refreshTokenString);

    refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDuration));
    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if(token.getExpiresAt().compareTo(Instant.now()) < 0) {
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
}
