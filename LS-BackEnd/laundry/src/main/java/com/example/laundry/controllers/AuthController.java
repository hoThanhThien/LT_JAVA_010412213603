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

  @Autowired
  private com.example.laundry.repository.UserRepository userRepository;
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
  public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
          @RequestBody RefreshTokenRequest request,
          jakarta.servlet.http.HttpServletRequest httpRequest) {
    try {
      if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      String deviceInfo = httpRequest != null ? httpRequest.getHeader("User-Agent") : null;
      String ipAddress = extractClientIp(httpRequest);

      // Token Rotation: tiêu thụ token cũ atomically và cấp refresh token mới + access token mới
      RefreshToken rotatedToken = refreshTokenService.rotateRefreshToken(
              request.getRefreshToken(),
              deviceInfo,
              ipAddress
      );
      User user = rotatedToken.getUser();
      String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRoles());

      return ResponseEntity.ok(new ApiResponse<>("", new RefreshTokenResponse(rotatedToken.getToken(), newAccessToken)));
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

  @PostMapping("/forgot-password")
  public ResponseEntity<com.example.laundry.utils.ApiResponse<java.util.Map<String, Object>>> forgotPassword(
          @RequestBody ForgotPasswordRequest request
  ) {
    return ResponseEntity.ok(authService.forgotPassword(request));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<com.example.laundry.utils.ApiResponse<Void>> resetPassword(
          @RequestBody ResetPasswordRequest request
  ) {
    return ResponseEntity.ok(authService.resetPassword(request));
  }

  @GetMapping("/sessions")
  public ResponseEntity<ApiResponse<java.util.List<java.util.Map<String, Object>>>> getActiveSessions(
          org.springframework.security.core.Authentication authentication
  ) {
    User user = requireAuthenticatedUser(authentication);
    return ResponseEntity.ok(new ApiResponse<>(
            "Lấy danh sách phiên đăng nhập thành công",
            refreshTokenService.getActiveSessions(user)
    ));
  }

  @DeleteMapping("/sessions/{id}")
  public ResponseEntity<ApiResponse<Void>> revokeSession(
          @PathVariable Long id,
          org.springframework.security.core.Authentication authentication
  ) {
    User user = requireAuthenticatedUser(authentication);
    boolean revoked = refreshTokenService.revokeSession(user, id);
    if (!revoked) {
      throw new org.springframework.web.server.ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Không tìm thấy phiên đăng nhập"
      );
    }
    return ResponseEntity.ok(new ApiResponse<>("Đã thu hồi phiên đăng nhập", null));
  }

  @DeleteMapping("/sessions")
  public ResponseEntity<ApiResponse<Void>> revokeAllSessions(
          org.springframework.security.core.Authentication authentication
  ) {
    User user = requireAuthenticatedUser(authentication);
    refreshTokenService.deleteByUser(user);
    return ResponseEntity.ok(new ApiResponse<>("Đã thu hồi toàn bộ phiên đăng nhập", null));
  }

  private User requireAuthenticatedUser(org.springframework.security.core.Authentication authentication) {
    User user = com.example.laundry.security.SecurityUtils.resolveCurrentUser(
            authentication,
            userRepository::findById,
            principal -> {
              User byPhone = userRepository.findByPhone(principal);
              if (byPhone != null) {
                return byPhone;
              }
              User byEmail = userRepository.findByEmail(principal);
              return byEmail != null ? byEmail : userRepository.findByUsername(principal);
            }
    );
    if (user == null) {
      throw new org.springframework.web.server.ResponseStatusException(
              HttpStatus.UNAUTHORIZED,
              "Yêu cầu đăng nhập"
      );
    }
    return user;
  }

  private String extractClientIp(jakarta.servlet.http.HttpServletRequest request) {
    if (request == null) {
      return null;
    }
    String forwardedFor = request.getHeader("X-Forwarded-For");
    if (forwardedFor != null && !forwardedFor.isBlank()) {
      return forwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
