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
