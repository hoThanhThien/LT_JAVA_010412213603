package com.example.laundry.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GetInfoResponse {
  private DataInfo data;
  private String message;

  public GetInfoResponse(DataInfo data, String message) {
    this.data = data;
    this.message = message;
  }

  @Data
  public static class DataInfo {
    private GetInfoResponse.AccountInfo account;

    public DataInfo(GetInfoResponse.AccountInfo account) {
      this.account = account;
    }
  }

  @Getter
  public static class AccountInfo {
    private final UUID id;
    private final String username;
    private final String phone;
    private final String email;
    private final String address;
    private final String avtUser;
    private final String role;

    public AccountInfo(UUID id, String username, String phone, String email, String address, String avtUser, String role) {
      this.id = id;
      this.username = username;
      this.phone = phone;
      this.email = email;
      this.address = address;
      this.avtUser = avtUser;
      this.role = role;
    }
  }
}
