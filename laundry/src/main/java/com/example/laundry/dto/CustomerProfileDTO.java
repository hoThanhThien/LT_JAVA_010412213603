package com.example.laundry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileDTO {
    private String username;
    private String email;
    private String phone;
    private String address;
    private String fullName;
    // Add other fields you want to allow customers to update
}
