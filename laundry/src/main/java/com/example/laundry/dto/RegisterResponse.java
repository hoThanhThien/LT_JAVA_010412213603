package com.example.laundry.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RegisterResponse {
  private String message;
  private AccountInfor accountInfor;

  public RegisterResponse(String message, AccountInfor userInfo) {
    this.message = message;
    this.accountInfor = userInfo;
  }

  public RegisterResponse(String message) {
    this.message = message;
    this.accountInfor = null;
  }

  @Data
  public static class AccountInfor {
    private UUID id;
    private String username;
    private String phone;
    private String role;

    public AccountInfor(UUID id, String username, String phone, String role) {
      this.id = id;
      this.username = username;
      this.phone = phone;
      this.role = role;
    }
  }
}
