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
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
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
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
  private static final String FIREBASE_PUBLIC_CERTS_URL =
          "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";
  private static final NetHttpTransport GOOGLE_HTTP_TRANSPORT = new NetHttpTransport();
  private static final GsonFactory GOOGLE_JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static final long OTP_TTL_SECONDS = 300;
  private static final int MAX_OTP_ATTEMPTS = 5;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public record GoogleUserInfo(String email, boolean emailVerified, String name, String picture) {}

  private record PasswordResetOtpEntry(String otpCode, Instant expiresAt, int failedAttempts) {}

  private final Map<UUID, PasswordResetOtpEntry> resetOtpStore = new ConcurrentHashMap<>();

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

  @Autowired
  private com.example.laundry.security.LoginRateLimiter loginRateLimiter;

  @Autowired(required = false)
  private FirebaseAuth firebaseAuth;

  @Autowired(required = false)
  private JavaMailSender mailSender;

  @Value("${firebase.project-id:fir-service-account-18466}")
  private String firebaseProjectId;

  @Value("${spring.mail.username:}")
  private String mailUsername;

  private volatile GoogleIdTokenVerifier firebaseTokenVerifier;
  private volatile GoogleIdTokenVerifier googleOAuthVerifier;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    if (loginRequest == null || loginRequest.getPhone() == null || loginRequest.getPhone().isBlank()
            || loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
      return new LoginResponse(null, "Số điện thoại hoặc mật khẩu không được để trống!");
    }

    String phone = loginRequest.getPhone().trim();
    String rateLimitKey = resolveClientIp() + ":" + phone;
    loginRateLimiter.checkAllowed(rateLimitKey);

    try {
      User user = userRepository.findByPhone(phone);
      if (user == null) {
        loginRateLimiter.recordFailure(rateLimitKey);
        return new LoginResponse(null, "Số điện thoại sai. Vui lòng nhập lại!!!!");
      }

      if (!isPasswordValid(user, loginRequest.getPassword())) {
        loginRateLimiter.recordFailure(rateLimitKey);
        return new LoginResponse(null, "Mật khẩu không đúng. Vui lòng nhập lại!!!");
      }

      // Nếu password là plaintext, mã hóa và lưu nó
      if (!user.getPassword().startsWith("$2a$") &&
              !user.getPassword().startsWith("$2b$") &&
              !user.getPassword().startsWith("$2y$")) {
        log.info("Upgrading legacy plaintext password hash for userId={}", user.getId());
        updatePasswordWithEncryption(user, loginRequest.getPassword());
      }

      loginRateLimiter.recordSuccess(rateLimitKey);
      return buildLoginResponse(user, "Đăng nhập thành công!!!");
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception e) {
      log.error("Đăng nhập lỗi: {}", e.getMessage(), e);
      throw new RuntimeException("Đăng nhập thất bại", e);
    }
  }

  private String resolveClientIp() {
    var attrs = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
    if (attrs instanceof org.springframework.web.context.request.ServletRequestAttributes servletAttrs) {
      jakarta.servlet.http.HttpServletRequest request = servletAttrs.getRequest();
      String forwardedFor = request.getHeader("X-Forwarded-For");
      if (forwardedFor != null && !forwardedFor.isBlank()) {
        return forwardedFor.split(",")[0].trim();
      }
      if (request.getRemoteAddr() != null && !request.getRemoteAddr().isBlank()) {
        return request.getRemoteAddr();
      }
    }
    return "local";
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
      return new LoginResponse(null, "Đăng nhập Google thất bại");
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

    if (request.getIdToken() != null && !request.getIdToken().isBlank()) {
      GoogleUserInfo verifiedFirebase = verifyWithGoogleVerifier(getFirebaseTokenVerifier(), request.getIdToken());
      if (verifiedFirebase != null) {
        return verifiedFirebase;
      }
    }

    String oauthToken = (request.getGoogleIdToken() != null && !request.getGoogleIdToken().isBlank())
            ? request.getGoogleIdToken()
            : request.getIdToken();
    if (oauthToken != null && !oauthToken.isBlank()) {
      return verifyWithGoogleVerifier(getGoogleOAuthVerifier(), oauthToken);
    }

    return null;
  }

  private GoogleIdTokenVerifier getFirebaseTokenVerifier() {
    GoogleIdTokenVerifier verifier = firebaseTokenVerifier;
    if (verifier == null) {
      synchronized (this) {
        verifier = firebaseTokenVerifier;
        if (verifier == null) {
          GooglePublicKeysManager keysManager = new GooglePublicKeysManager.Builder(
                  GOOGLE_HTTP_TRANSPORT, GOOGLE_JSON_FACTORY)
                  .setPublicCertsEncodedUrl(FIREBASE_PUBLIC_CERTS_URL)
                  .build();
          String projectId = (firebaseProjectId != null && !firebaseProjectId.isBlank())
                  ? firebaseProjectId
                  : "fir-service-account-18466";
          verifier = new GoogleIdTokenVerifier.Builder(keysManager)
                  .setIssuer("https://securetoken.google.com/" + projectId)
                  .setAudience(Collections.singletonList(projectId))
                  .build();
          firebaseTokenVerifier = verifier;
        }
      }
    }
    return verifier;
  }

  private GoogleIdTokenVerifier getGoogleOAuthVerifier() {
    GoogleIdTokenVerifier verifier = googleOAuthVerifier;
    if (verifier == null) {
      synchronized (this) {
        verifier = googleOAuthVerifier;
        if (verifier == null) {
          verifier = new GoogleIdTokenVerifier.Builder(GOOGLE_HTTP_TRANSPORT, GOOGLE_JSON_FACTORY)
                  .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                  .build();
          googleOAuthVerifier = verifier;
        }
      }
    }
    return verifier;
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
      log.warn("GoogleIdTokenVerifier verification failed: {}", e.getMessage());
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
      return new LogoutResponse("Logout thất bại");
    }
  }

  @Override
  public ApiResponse<Map<String, Object>> forgotPassword(ForgotPasswordRequest request) {
    if (request == null || request.getIdentifier() == null || request.getIdentifier().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập email hoặc số điện thoại");
    }

    User user = findUserByIdentifier(request.getIdentifier());
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản với email hoặc số điện thoại này");
    }

    Instant now = Instant.now();
    resetOtpStore.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));

    String otp = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    resetOtpStore.put(user.getId(), new PasswordResetOtpEntry(otp, now.plus(Duration.ofSeconds(OTP_TTL_SECONDS)), 0));

    String targetEmail = user.getEmail() != null ? user.getEmail().trim() : "";
    boolean hasEmail = !targetEmail.isBlank();
    boolean emailSent = false;

    if (hasEmail && mailSender != null && mailUsername != null && !mailUsername.isBlank()) {
      try {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailUsername.trim());
        mailMessage.setTo(targetEmail);
        mailMessage.setSubject("[HERAMO] Mã OTP khôi phục mật khẩu");
        mailMessage.setText(
                "Xin chào " + user.getUsername() + ",\n\n"
                        + "Mã OTP khôi phục mật khẩu của bạn là: " + otp + "\n"
                        + "Mã có hiệu lực trong 5 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai.\n\n"
                        + "Trân trọng,\nHERAMO Laundry"
        );
        mailSender.send(mailMessage);
        emailSent = true;
      } catch (Exception ex) {
        log.warn("Không thể gửi email OTP khôi phục mật khẩu tới {}: {}", targetEmail, ex.getMessage());
      }
    }

    String maskedEmail = hasEmail ? maskEmail(targetEmail) : null;
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("emailSent", emailSent);
    data.put("maskedEmail", maskedEmail);
    data.put("expiresInSeconds", OTP_TTL_SECONDS);
    if (!emailSent) {
      data.put("devOtp", otp);
    }

    String message;
    if (emailSent) {
      message = "Mã OTP khôi phục mật khẩu đã được gửi tới email " + maskedEmail;
    } else if (hasEmail) {
      message = "Không thể gửi email tự động lúc này, vui lòng sử dụng mã OTP hiển thị trên màn hình (hiệu lực 5 phút)";
    } else {
      message = "Tài khoản chưa liên kết email, vui lòng sử dụng mã OTP hiển thị trên màn hình (hiệu lực 5 phút)";
    }

    return new ApiResponse<>(message, data);
  }

  @Override
  @Transactional
  public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {
    if (request == null || request.getIdentifier() == null || request.getIdentifier().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập email hoặc số điện thoại");
    }
    if (request.getOtp() == null || request.getOtp().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập mã OTP");
    }
    if (request.getNewPassword() == null || !UserValidator.isValidPassword(request.getNewPassword())) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Mật khẩu mới phải từ 8 ký tự trở lên, gồm chữ, số và ký tự đặc biệt (@#$%^&+=!*._?-)"
      );
    }
    if (request.getConfirmPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xác nhận mật khẩu không khớp");
    }

    User user = findUserByIdentifier(request.getIdentifier());
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản với email hoặc số điện thoại này");
    }

    PasswordResetOtpEntry entry = resetOtpStore.get(user.getId());
    if (entry == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn chưa yêu cầu mã OTP hoặc mã đã hết hạn");
    }
    if (Instant.now().isAfter(entry.expiresAt())) {
      resetOtpStore.remove(user.getId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
    }

    String submittedOtp = request.getOtp().trim();
    if (!entry.otpCode().equals(submittedOtp)) {
      int nextAttempts = entry.failedAttempts() + 1;
      if (nextAttempts >= MAX_OTP_ATTEMPTS) {
        resetOtpStore.remove(user.getId());
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Bạn đã nhập sai mã OTP quá 5 lần. Vui lòng yêu cầu mã mới."
        );
      }
      resetOtpStore.put(user.getId(), new PasswordResetOtpEntry(entry.otpCode(), entry.expiresAt(), nextAttempts));
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Mã OTP không chính xác (còn " + (MAX_OTP_ATTEMPTS - nextAttempts) + " lần thử)"
      );
    }

    updatePasswordWithEncryption(user, request.getNewPassword());
    refreshTokenService.deleteByUser(user);
    resetOtpStore.remove(user.getId());
    return new ApiResponse<>("Đặt lại mật khẩu thành công");
  }

  private User findUserByIdentifier(String rawIdentifier) {
    String trimmed = rawIdentifier.trim();
    String normalizedEmail = trimmed.toLowerCase(Locale.ROOT);
    User user = userRepository.findByEmail(normalizedEmail);
    if (user == null && !trimmed.equals(normalizedEmail)) {
      user = userRepository.findByEmail(trimmed);
    }
    if (user == null) {
      user = userRepository.findByPhone(trimmed);
    }
    return user;
  }

  private String maskEmail(String email) {
    int atIndex = email.indexOf('@');
    if (atIndex <= 0) {
      return "***";
    }
    String localPart = email.substring(0, atIndex);
    String domainPart = email.substring(atIndex);
    if (localPart.length() <= 2) {
      return localPart.charAt(0) + "***" + domainPart;
    }
    return localPart.substring(0, 2) + "***" + domainPart;
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