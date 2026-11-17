package com.example.laundry.services;

import com.example.laundry.dto.*;

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
  LoginResponse loginWithGoogle(GoogleLoginRequest googleLoginRequest);
  LogoutResponse logout(LogoutRequest logoutRequest);
//  GetInfoResponse getInfo(GetInfoRequest getInfoRequest);
//  RegisterResponse register(RegisterRequest registerRequest);
}
