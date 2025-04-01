package com.example.laundry.controllers;

import com.example.laundry.dto.LoginRequest;
import com.example.laundry.dto.LoginResponse;
import com.example.laundry.dto.RefreshTokenRequest;
import com.example.laundry.dto.RefreshTokenResponse;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.RefreshTokenRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.RefreshTokenService;
import com.example.laundry.services.impl.TokenBlackListServiceImpl;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @Autowired
  private TokenBlackListServiceImpl tokenBlackListService;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      if (authService.logout(token)) {
        return ResponseEntity.ok("Logout thành công!!!");
      }
    }
    return ResponseEntity.badRequest().body("Token không hợp lệ hoặc không tìm thấy");
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    try {
      //Kiểm tra refresh token xem tồn tại và còn hạn không
      RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
      refreshTokenService.verifyExpiration(refreshToken);

      //Tạo access token mới
      User user = refreshToken.getUser();
      String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());

      return ResponseEntity.ok(new RefreshTokenResponse(refreshToken.getToken(), newAccessToken));
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}
