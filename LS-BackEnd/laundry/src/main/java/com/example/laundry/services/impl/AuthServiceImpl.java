package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.RefreshTokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
  private static final String FIREBASE_PUBLIC_CERTS_URL =
          "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";

  public record GoogleUserInfo(String email, boolean emailVerified, String name, String picture) {}

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenBlackListServiceImpl tokenBlackListServiceImpl;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired(required = false)
  private FirebaseAuth firebaseAuth;

  @Value("${firebase.project-id:heramo-b5a4c}")
  private String firebaseProjectId;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    try {
      if (loginRequest == null || loginRequest.getPhone() == null || loginRequest.getPhone().isBlank()
              || loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
        return new LoginResponse(null, "Số điện thoại hoặc mật khẩu không được để trống!");
      }

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

      return buildLoginResponse(user, "Đăng nhập thành công!!!");
    } catch (Exception e) {
      log.error("Đăng nhập lỗi: {}", e.getMessage(), e);
      throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public LoginResponse loginWithGoogle(GoogleLoginRequest request) {
    try {
      if (request == null
              || ((request.getIdToken() == null || request.getIdToken().isBlank())
              && (request.getGoogleIdToken() == null || request.getGoogleIdToken().isBlank()))) {
        return new LoginResponse(null, "Thiếu token xác thực Google");
      }

      GoogleUserInfo googleUser = verifyGoogleToken(request);
      if (googleUser == null || googleUser.email() == null || googleUser.email().isBlank() || !googleUser.emailVerified()) {
        return new LoginResponse(null, "Token Google không hợp lệ hoặc email chưa được xác thực");
      }

      String trimmedEmail = googleUser.email().trim();
      String normalizedEmail = trimmedEmail.toLowerCase(Locale.ROOT);

      User user = userRepository.findByEmail(normalizedEmail);
      if (user == null && !trimmedEmail.equals(normalizedEmail)) {
        user = userRepository.findByEmail(trimmedEmail);
      }

      String message;
      if (user == null) {
        String displayName = (googleUser.name() != null && !googleUser.name().isBlank())
                ? googleUser.name().trim()
                : normalizedEmail.split("@")[0];
        if (displayName.length() > 100) {
          displayName = displayName.substring(0, 100);
        }

        Customer customer = new Customer();
        customer.setUsername(displayName);
        customer.setEmail(normalizedEmail);
        customer.setPhone(null);
        customer.setPassword(passwordEncoder.encode(UUID.randomUUID().toString() + "@G1a"));
        customer.setRoles(Roles.Customer);
        if (googleUser.picture() != null && !googleUser.picture().isBlank()) {
          customer.setAvtUser(googleUser.picture());
        }
        user = customerRepository.save(customer);
        message = "Đăng ký và đăng nhập bằng Google thành công!!!";
      } else {
        if ((user.getAvtUser() == null || user.getAvtUser().isBlank())
                && googleUser.picture() != null && !googleUser.picture().isBlank()) {
          user.setAvtUser(googleUser.picture());
          user = userRepository.save(user);
        }
        message = "Đăng nhập bằng Google thành công!!!";
      }

      return buildLoginResponse(user, message);
    } catch (Exception e) {
      log.error("Đăng nhập Google thất bại: {}", e.getMessage(), e);
      return new LoginResponse(null, "Đăng nhập Google thất bại: " + e.getMessage());
    }
  }

  protected GoogleUserInfo verifyGoogleToken(GoogleLoginRequest request) {
    if (firebaseAuth != null && request.getIdToken() != null && !request.getIdToken().isBlank()) {
      try {
        FirebaseToken decoded = firebaseAuth.verifyIdToken(request.getIdToken());
        return new GoogleUserInfo(decoded.getEmail(), decoded.isEmailVerified(), decoded.getName(), decoded.getPicture());
      } catch (Exception e) {
        log.debug("FirebaseAuth verifyIdToken failed, falling back to public certificate verifier: {}", e.getMessage());
      }
    }

    NetHttpTransport transport = new NetHttpTransport();
    GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    if (request.getIdToken() != null && !request.getIdToken().isBlank()) {
      GooglePublicKeysManager keysManager = new GooglePublicKeysManager.Builder(transport, jsonFactory)
              .setPublicCertsEncodedUrl(FIREBASE_PUBLIC_CERTS_URL)
              .build();
      GoogleIdTokenVerifier firebaseVerifier = new GoogleIdTokenVerifier.Builder(keysManager)
              .setIssuer("https://securetoken.google.com/" + firebaseProjectId)
              .setAudience(Collections.singletonList(firebaseProjectId))
              .build();
      GoogleUserInfo verifiedFirebase = verifyWithGoogleVerifier(firebaseVerifier, request.getIdToken());
      if (verifiedFirebase != null) {
        return verifiedFirebase;
      }
    }

    String oauthToken = (request.getGoogleIdToken() != null && !request.getGoogleIdToken().isBlank())
            ? request.getGoogleIdToken()
            : request.getIdToken();
    if (oauthToken != null && !oauthToken.isBlank()) {
      GoogleIdTokenVerifier googleVerifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
              .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
              .build();
      return verifyWithGoogleVerifier(googleVerifier, oauthToken);
    }

    return null;
  }

  private GoogleUserInfo verifyWithGoogleVerifier(GoogleIdTokenVerifier verifier, String rawToken) {
    try {
      GoogleIdToken idToken = verifier.verify(rawToken);
      if (idToken == null) {
        return null;
      }
      GoogleIdToken.Payload payload = idToken.getPayload();
      return new GoogleUserInfo(
              payload.getEmail(),
              Boolean.TRUE.equals(payload.getEmailVerified()),
              (String) payload.get("name"),
              (String) payload.get("picture")
      );
    } catch (Exception e) {
      log.debug("GoogleIdTokenVerifier verification failed: {}", e.getMessage());
      return null;
    }
  }

  private LoginResponse buildLoginResponse(User user, String message) {
    String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRoles());
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
    String refreshTokenString = refreshToken.getToken();

    if (accessToken == null) {
      log.error("Generated access token is null for userId={}", user.getId());
      throw new RuntimeException("Failed to generate token");
    }

    LoginResponse.AccountInfo accountInfo = new LoginResponse.AccountInfo(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRoles().name(),
            user.getPhone()
    );

    LoginResponse.DataInfo dataInfo = new LoginResponse.DataInfo(accessToken, refreshTokenString, accountInfo);
    return new LoginResponse(dataInfo, message);
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