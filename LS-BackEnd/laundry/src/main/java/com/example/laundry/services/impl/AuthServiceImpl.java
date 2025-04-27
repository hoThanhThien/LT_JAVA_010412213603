package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AuthService;
import com.example.laundry.services.RefreshTokenService;
import com.example.laundry.utils.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

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
  @Autowired
  private FirebaseAuth firebaseAuth;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    try {
      User phone = userRepository.findByPhone(loginRequest.getPhone());
      if (phone == null) {
        return new LoginResponse(null, "Số điện thoại sai. Vui lòng nhập lại!!!!");
      }

      if (!isPasswordValid(phone, loginRequest.getPassword())) {
        return new LoginResponse(null, "Mật khẩu không đúng. Vui lòng nhập lại!!!");
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
        String accessToken = jwtUtil.generateAccessToken(phone.getUsername(), phone.getRoles());

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
  @Transactional
  public LogoutResponse logout(LogoutRequest logoutRequest) {
    try {
      if(logoutRequest.getRefreshToken() != null) {
        RefreshToken refreshToken = refreshTokenService.findByToken(logoutRequest.getRefreshToken());

        String accessToken = logoutRequest.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
          tokenBlackListServiceImpl.addBlacklistToken(accessToken);
        }
        refreshTokenService.deleteByToken(refreshToken.getToken());
        return new LogoutResponse("Logout thành công!!!");
      }
      return new LogoutResponse("Refresh token không tìm thấy");
    }
    catch (Exception e) {
      return new LogoutResponse("Logout thất bại: " + e.getMessage());
    }
  }

//  @Override
//  @Transactional
//  public RegisterResponse register(RegisterRequest registerRequest) {
//    // Xác minh idToken từ firebase để lấy số điện thoại
//    try {
//      FirebaseToken decodedToken = firebaseAuth.verifyIdToken(registerRequest.getIdToken());
//      String verifiedPhone = null;
//
//      Map<String, Object> claims = decodedToken.getClaims();
//
//      if (claims.containsKey("phone")) {
//        verifiedPhone = (String) claims.get("phone");
//      }
//
//      if (verifiedPhone == null || verifiedPhone.isEmpty()) {
//        return new RegisterResponse("Số điện thoại đã được dùng rồi");
//      }
//
//      //Tạo người dùng
//      Customer customer = new Customer();
//      customer.setPhone(verifiedPhone);
//      customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//      customer.setUsername(registerRequest.getUsername());
//      customer.setRoles(Roles.Customer);
//
//      Customer savedCustomer = userRepository.save(customer);
//
//      RegisterResponse.AccountInfor accountInfor = new RegisterResponse.AccountInfor(
//              savedCustomer.getId(),
//              savedCustomer.getUsername(),
//              savedCustomer.getPhone(),
//              savedCustomer.getRoles().name()
//      );
//
//      return new RegisterResponse("Đăng ký thành công!!!",  accountInfor);
//    } catch (FirebaseAuthException e) {
//      System.err.println("Firebase authentication error: " + e.getMessage());
//      return new RegisterResponse("Xác minh Firebase thất bại: " + e.getMessage());
//    } catch (Exception e) {
//      System.err.println("Registration error: " + e.getMessage());
//      e.printStackTrace();
//      return new RegisterResponse("Đăng ký thất bại: " + e.getMessage());
//    }
//  }

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