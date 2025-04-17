package com.example.laundry.services.impl;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public GetInfoResponse getInfo(GetInfoRequest getInfoRequest) {
    try{
      String accessToken = getInfoRequest.getAccessToken();
      if(accessToken == null || accessToken.isEmpty()) {
        System.err.println("Access token is null!");
        return new GetInfoResponse(null, "Access không được để trống!!!");
      }

      // Lấy thông tin người dùng
      String username = jwtUtil.validateTokenAndRetrieveSubject(accessToken);
      User user = userRepository.findByUsername(username);
      if (user == null) {
        return new GetInfoResponse(null, "Không tìm thấy thông tin người dùng!!!");
      }
      GetInfoResponse.AccountInfo accountInfo = new GetInfoResponse.AccountInfo(
              user.getId(),
              user.getUsername(),
              user.getEmail(),
              user.getRoles().name(),
              user.getPhone()
      );
      GetInfoResponse.DataInfo dataInfo = new GetInfoResponse.DataInfo(accountInfo);
      return new GetInfoResponse(dataInfo, "Lấy thông tin thành công!!!");
    }
    catch (Exception e) {
      System.err.println("Lấy thông tin lỗi: " + e.getMessage());
      e.printStackTrace();
      return new GetInfoResponse(null, "Lấy thông tin thật bại!!!");
    }
  }
}
