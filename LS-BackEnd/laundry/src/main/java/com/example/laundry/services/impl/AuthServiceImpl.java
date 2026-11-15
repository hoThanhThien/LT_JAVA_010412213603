package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenBlackListServiceImpl tokenBlackListServiceImpl;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    try {
      User user = userRepository.findByPhone(loginRequest.getPhone());
      if (user == null) {
        return new LoginResponse(null, "Số điện thoại sai. Vui lòng nhập lại!!!!");
      }

      if (!isPasswordValid(user, loginRequest.getPassword())) {
        return new LoginResponse(null, "Mật khẩu không đúng. Vui lòng nhập lại!!!");
      }

      // Nếu password là plaintext, mã hóa và lưu nó
      if (!user.getPassword().startsWith("$2a$") &&
              !user.getPassword().startsWith("$2b$") &&
              !user.getPassword().startsWith("$2y$")) {
        log.info("Upgrading legacy plaintext password hash for userId={}", user.getId());
        updatePasswordWithEncryption(user, loginRequest.getPassword());
      }

      // Tạo accessToken (gắn userId bất biến)
      String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRoles());

      // Tạo refreshToken
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
      String refreshTokenString = refreshToken.getToken();

      if (accessToken == null) {
        log.error("Generated access token is null for userId={}", user.getId());
        throw new RuntimeException("Failed to generate token");
      }

      // Lấy thông tin người dùng
      LoginResponse.AccountInfo accountInfo = new LoginResponse.AccountInfo(
              user.getId(),
              user.getUsername(),
              user.getEmail(),
              user.getRoles().name(),
              user.getPhone()
      );

      LoginResponse.DataInfo dataInfo = new LoginResponse.DataInfo(accessToken, refreshTokenString, accountInfo);

      return new LoginResponse(dataInfo, "Đăng nhập thành công!!!");
    } catch (Exception e) {
      log.error("Đăng nhập lỗi: {}", e.getMessage(), e);
      throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public LogoutResponse logout(LogoutRequest logoutRequest) {
    try {
      if (logoutRequest == null) {
        return new LogoutResponse("Yêu cầu đăng xuất không hợp lệ");
      }

      boolean revoked = false;
      String accessToken = logoutRequest.getAccessToken();
      if (accessToken != null && !accessToken.isBlank()) {
        tokenBlackListServiceImpl.addBlacklistToken(accessToken);
        revoked = true;
      }

      String rawRefreshToken = logoutRequest.getRefreshToken();
      if (rawRefreshToken != null && !rawRefreshToken.isBlank()) {
        try {
          RefreshToken refreshToken = refreshTokenService.findByToken(rawRefreshToken);
          refreshTokenService.deleteByToken(refreshToken.getToken());
          revoked = true;
        } catch (Exception ex) {
          log.warn("Không thể thu hồi refresh token khi logout (có thể đã hết hạn hoặc bị xóa): {}", ex.getMessage());
        }
      }

      if (revoked) {
        return new LogoutResponse("Logout thành công!!!");
      }
      return new LogoutResponse("Refresh token không tìm thấy");
    } catch (Exception e) {
      log.error("Logout thất bại: {}", e.getMessage(), e);
      return new LogoutResponse("Logout thất bại: " + e.getMessage());
    }
  }

  private boolean isPasswordValid(User user, String rawPassword) {
    if (user.getPassword().startsWith("$2a$") ||
            user.getPassword().startsWith("$2b$") ||
            user.getPassword().startsWith("$2y$")) {
      // Nếu password đã được mã hóa, so sánh bằng passwordEncoder
      return passwordEncoder.matches(rawPassword, user.getPassword());
    } else {
      // Nếu password chưa mã hóa, so sánh trực tiếp
      return user.getPassword().equals(rawPassword);
    }
  }

  private void updatePasswordWithEncryption(User user, String rawPassword) {
    user.setPassword(passwordEncoder.encode(rawPassword));
    userRepository.save(user);
  }
}