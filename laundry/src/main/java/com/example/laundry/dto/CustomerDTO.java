package com.example.laundry.dto;

import com.example.laundry.models.user.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Roles role;
    private Date createdAt;
    private Date updatedAt;
}
