package com.example.laundry.services;

import com.example.laundry.dto.LoginRequest;
import com.example.laundry.dto.LoginResponse;

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
  boolean logout(String token);
}
