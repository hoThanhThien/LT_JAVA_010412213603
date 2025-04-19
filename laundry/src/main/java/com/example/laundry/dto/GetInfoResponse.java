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
    private final String email;
    private final String role;
    private final String phone;

    public AccountInfo(UUID id, String username, String email, String role, String phone) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.role = role;
      this.phone = phone;
    }
  }
}
