package com.example.laundry.services;

<<<<<<< HEAD
import com.example.laundry.dto.*;
=======
import com.example.laundry.dto.LoginRequest;
import com.example.laundry.dto.LoginResponse;
import com.example.laundry.dto.LogoutRequest;
import com.example.laundry.dto.LogoutResponse;
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
  LogoutResponse logout(LogoutRequest logoutRequest);
<<<<<<< HEAD
  GetInfoResponse getInfo(GetInfoRequest getInfoRequest);
//  RegisterResponse register(RegisterRequest registerRequest);
=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
}
