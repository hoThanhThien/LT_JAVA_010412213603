package com.example.laundry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
  private String identifier;
  private String otp;
  private String newPassword;
  private String confirmPassword;
}
