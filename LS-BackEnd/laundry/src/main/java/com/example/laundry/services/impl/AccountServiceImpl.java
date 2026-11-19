package com.example.laundry.services.impl;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.security.SecurityUtils;
import com.example.laundry.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountServiceImpl implements AccountService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

  @Autowired
  private com.example.laundry.services.RefreshTokenService refreshTokenService;

  @Override
  public GetInfoResponse getInfo(GetInfoRequest getInfoRequest) {
    try {
      String accessToken = getInfoRequest.getAccessToken();
      if (accessToken == null || accessToken.isEmpty()) {
        return new GetInfoResponse(null, "Access không được để trống!!!");
      }

      String principalId = jwtUtil.validateAccessTokenAndRetrieveSubject(accessToken);
      User user = SecurityUtils.parseUuid(principalId)
              .flatMap(userRepository::findById)
              .orElseGet(() -> userRepository.findByUsername(principalId));
      if (user == null) {
        return new GetInfoResponse(null, "Không tìm thấy thông tin người dùng!!!");
      }

      return buildInfoResponse(user);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
    }
  }

  @Override
  @org.springframework.transaction.annotation.Transactional
  public com.example.laundry.utils.ApiResponse<Void> changePassword(
          Authentication authentication,
          com.example.laundry.dto.ChangePasswordRequest request
  ) {
    com.example.laundry.models.user.User user = com.example.laundry.security.SecurityUtils.resolveCurrentUser(
            authentication,
            userRepository::findById,
            principal -> {
              com.example.laundry.models.user.User byPhone = userRepository.findByPhone(principal);
              if (byPhone != null) {
                return byPhone;
              }
              com.example.laundry.models.user.User byEmail = userRepository.findByEmail(principal);
              return byEmail != null ? byEmail : userRepository.findByUsername(principal);
            }
    );
    if (user == null) {
      throw new org.springframework.web.server.ResponseStatusException(
              org.springframework.http.HttpStatus.UNAUTHORIZED,
              "Chưa đăng nhập hoặc không tìm thấy tài khoản"
      );
    }

    if (request == null || request.getNewPassword() == null
            || !com.example.laundry.utils.UserValidator.isValidPassword(request.getNewPassword())) {
      throw new org.springframework.web.server.ResponseStatusException(
              org.springframework.http.HttpStatus.BAD_REQUEST,
              "Mật khẩu mới phải từ 8 ký tự trở lên, gồm chữ, số và ký tự đặc biệt (@#$%^&+=!*._?-)"
      );
    }
    if (request.getConfirmPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new org.springframework.web.server.ResponseStatusException(
              org.springframework.http.HttpStatus.BAD_REQUEST,
              "Xác nhận mật khẩu mới không khớp"
      );
    }

    String currentPassword = request.getCurrentPassword();
    boolean hasCurrentPassword = currentPassword != null && !currentPassword.isBlank();
    boolean hasPhone = user.getPhone() != null && !user.getPhone().isBlank();

    if (hasCurrentPassword) {
      String storedPassword = user.getPassword();
      boolean matches = storedPassword != null && (
              (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$"))
                      ? passwordEncoder.matches(currentPassword, storedPassword)
                      : storedPassword.equals(currentPassword)
      );
      if (!matches) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Mật khẩu hiện tại không chính xác"
        );
      }
      if (request.getNewPassword().equals(currentPassword)) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Mật khẩu mới không được trùng với mật khẩu hiện tại"
        );
      }
    } else if (hasPhone) {
      throw new org.springframework.web.server.ResponseStatusException(
              org.springframework.http.HttpStatus.BAD_REQUEST,
              "Vui lòng nhập mật khẩu hiện tại (hoặc dùng Quên mật khẩu qua OTP)"
      );
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    refreshTokenService.deleteByUser(user);
    return new com.example.laundry.utils.ApiResponse<>("Đổi mật khẩu thành công");
  }

  @Override
  public GetInfoResponse getCurrentUserInfo(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
    }
    User user = SecurityUtils.resolveCurrentUser(authentication, userRepository::findById, userRepository::findByUsername);
    return buildInfoResponse(user);
  }

  private GetInfoResponse buildInfoResponse(User user) {
    GetInfoResponse.AccountInfo accountInfo = new GetInfoResponse.AccountInfo(
            user.getId(),
            user.getUsername(),
            user.getPhone(),
            user.getEmail(),
            user.getAddress(),
            user.getAvtUser(),
            user.getRoles().name()
    );
    GetInfoResponse.DataInfo dataInfo = new GetInfoResponse.DataInfo(accountInfo);
    return new GetInfoResponse(dataInfo, "Lấy thông tin thành công!!!");
  }
}
