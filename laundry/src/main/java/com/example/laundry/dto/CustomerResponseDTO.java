package com.example.laundry.dto;

<<<<<<< HEAD
import com.example.laundry.models.user.Customer;
=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
import com.example.laundry.models.user.Roles;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResponseDTO {
<<<<<<< HEAD
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
=======
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private Roles roles;
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
