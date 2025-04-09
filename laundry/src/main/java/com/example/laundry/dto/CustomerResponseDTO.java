package com.example.laundry.dto;

import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResponseDTO {
    private String message;
    private DataInfo data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataInfo {
        private String accessToken;
        private String refreshToken;
        private AccountInfo account;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountInfo {
        private UUID id;
        private String username;
        private String email;
        private String phone;
        private String address;
        private Roles roles;
    }
}