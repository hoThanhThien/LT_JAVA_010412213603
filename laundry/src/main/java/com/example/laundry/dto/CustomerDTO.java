package com.example.laundry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private boolean emailVerified;
}
