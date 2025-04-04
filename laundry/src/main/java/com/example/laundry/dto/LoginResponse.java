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

    public static class AccountInfo {
        @Getter
        private UUID id;
        @Getter
        private String username;
        @Getter
        private String email;
        @Getter
        private String role;
        @Getter
        private String phone;

        private final Boolean emailVerified;

        public AccountInfo(UUID id, String username, String email, String role, String phone, boolean emailVerified) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
            this.phone = phone;
            this.emailVerified = emailVerified;
        }

        public Boolean getEmailVerified() {
            return emailVerified;
        }
    }
}
