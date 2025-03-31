package com.example.laundry.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
