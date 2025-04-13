package com.example.laundry.dto;

import com.example.laundry.models.user.Employee;
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
    private String usernameStoreOwner;

    public EmployeeDTO(Employee employee) {
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = employee.getAddress();
        this.usernameStoreOwner = employee.getStoreOwner().getUsername();
    }
}
