package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.utils.ApiResponse;

import java.util.Map;

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
  LoginResponse loginWithGoogle(GoogleLoginRequest googleLoginRequest);
  LogoutResponse logout(LogoutRequest logoutRequest);
  ApiResponse<Map<String, Object>> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
  ApiResponse<Void> resetPassword(ResetPasswordRequest resetPasswordRequest);
//  GetInfoResponse getInfo(GetInfoRequest getInfoRequest);
//  RegisterResponse register(RegisterRequest registerRequest);
}
