package com.example.laundry.services;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import org.springframework.security.core.Authentication;

public interface AccountService {
  GetInfoResponse getInfo(GetInfoRequest getInfoRequest);

  GetInfoResponse getCurrentUserInfo(Authentication authentication);
}
