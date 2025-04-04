package com.example.laundry.dto;

import com.example.laundry.models.user.Roles;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private Roles roles;
}
