package com.example.laundry.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class LoginResponse {
    private DataInfo data;
    private String message;

    public LoginResponse(String accessToken, String refreshToken, AccountInfo account, String message) {
        this.data = new DataInfo(accessToken, refreshToken, account);
        this.message = message;
    }

    public LoginResponse(DataInfo dataInfo, String loginSuccessful) {
        this.data = dataInfo;
        this.message = loginSuccessful;
    }

    @Data
    public static class DataInfo {
        private String accessToken;
        private String refreshToken;
        private AccountInfo account;

        public DataInfo(String accessToken, String refreshToken, AccountInfo account) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
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
