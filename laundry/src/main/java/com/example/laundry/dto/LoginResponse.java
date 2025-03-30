package com.example.laundry.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private Long id;
        @Getter
        private String name;
        @Getter
        private String email;
        @Getter
        private String role;
        private final Boolean emailVerified;

        public AccountInfo(Long id, String name, String email, String role, boolean emailVerified) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.emailVerified = emailVerified;
        }

        public Boolean getEmailVerified() {
            return emailVerified;
        }
    }
}
