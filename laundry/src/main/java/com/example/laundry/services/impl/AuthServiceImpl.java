package com.example.laundry.services.impl;

import com.example.laundry.dto.LoginRequest;
import com.example.laundry.dto.LoginResponse;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Relation;

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
      User phone = userRepository.findByPhone(loginRequest.getPhone());
      if (phone == null) {
        System.out.println("Số điện thoại không tồn tại: " + loginRequest.getPhone());
        throw new RuntimeException("Số điện thoại hoặc mật khẩu không đúng");
      }

      if (isPasswordValid(phone, loginRequest.getPassword())) {
        // Nếu password là plaintext, mã hóa và lưu nó
        if (!phone.getPassword().startsWith("$2a$") &&
                !phone.getPassword().startsWith("$2b$") &&
                !phone.getPassword().startsWith("$2y$")) {
          System.out.println("Updating password with encryption");
          updatePasswordWithEncryption(phone, loginRequest.getPassword());
        }

        // Tạo accessToken
        String accessToken = jwtUtil.generateAccessToken(phone.getUsername());

        //Tạo refreshToken
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(phone);
        String refreshTokenString = refreshToken.getToken();

        if (accessToken == null) {
          System.err.println("Generated token is null!");
          throw new RuntimeException("Failed to generate token");
        }

        //Lấy thông tin người dùng
        LoginResponse.AccountInfo accountInfo = new LoginResponse.AccountInfo(
                phone.getId(),
                phone.getUsername(),
                phone.getEmail(),
                phone.getRoles().name(),
                phone.getPhone()
        );

        LoginResponse.DataInfo dataInfo = new LoginResponse.DataInfo(accessToken, refreshTokenString, accountInfo);

        return new LoginResponse(dataInfo, "Đăng nhập thành công!!!");
      } else {
        System.out.println("Lỗi mật khẩu với người dùng: " + phone.getUsername());
        throw new RuntimeException("Lỗi mật khẩu hoặc người dùng");
      }
    } catch (Exception e) {
      System.err.println("Đăng nhập lỗi: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage(), e);
    }
  }

  @Override
  public boolean logout(String token) {
    try{
      if(token != null) {
        tokenBlackListServiceImpl.isBlacklisted(token);
        return true;
      }
      return false;
    }
    catch (Exception e){
      System.err.println("Đăng xuất lỗi: " + e.getMessage());
      e.printStackTrace();
      return false;
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