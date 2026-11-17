package com.example.laundry.controllers;

import com.example.laundry.dto.*;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.RefreshTokenService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private CustomerService customerService;
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse response = authService.login(loginRequest);
    if (response.getData() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/google")
  public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
    LoginResponse response = authService.loginWithGoogle(request);
    if (response.getData() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(
          @RequestBody(required = false) LogoutRequest logoutRequest,
          @RequestHeader(value = "Authorization", required = false) String authHeader) {
    LogoutRequest effectiveRequest = logoutRequest != null ? logoutRequest : new LogoutRequest();
    if ((effectiveRequest.getAccessToken() == null || effectiveRequest.getAccessToken().isBlank())
            && authHeader != null && authHeader.startsWith("Bearer ")) {
      effectiveRequest.setAccessToken(authHeader.substring(7));
    }
    LogoutResponse response = authService.logout(effectiveRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
    try {
      if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      // Xác thực chữ ký JWT và claim tokenType=refresh trước khi tra DB
      jwtUtil.validateRefreshToken(request.getRefreshToken());

      //Kiểm tra refresh token xem tồn tại và còn hạn không
      RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
      refreshTokenService.verifyExpiration(refreshToken);

      //Tạo access token mới
      User user = refreshToken.getUser();
      String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRoles());

      return ResponseEntity.ok(new ApiResponse<>("", new RefreshTokenResponse(refreshToken.getToken(), newAccessToken)));
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
  @PostMapping("/register")
  public ResponseEntity<CustomerResponseDTO> register(@RequestBody RegisterRequest registerRequest) {
    CustomerResponseDTO response = customerService.register(registerRequest);
    if (response.getData() == null) {
      return ResponseEntity.badRequest().body(response);
    }
    return ResponseEntity.ok(response);
  }
}
