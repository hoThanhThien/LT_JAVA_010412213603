package com.example.laundry.services;

import com.example.laundry.dto.LoginRequest;
import com.example.laundry.dto.LoginResponse;
import com.example.laundry.dto.LogoutRequest;
import com.example.laundry.dto.LogoutResponse;

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
  LogoutResponse logout(LogoutRequest logoutRequest);
}
