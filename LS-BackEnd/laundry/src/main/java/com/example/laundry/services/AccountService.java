package com.example.laundry.services;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;

public interface AccountService {
  GetInfoResponse getInfo(GetInfoRequest getInfoRequest);
}
