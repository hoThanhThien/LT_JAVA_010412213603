package com.example.laundry.dto;

import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
    private Roles role;
    private byte[] avtUser;
    private Date createdAt;
    private Date updatedAt;
    private String usernameStoreOwner;

    public EmployeeDTO(Employee employee) {
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = employee.getAddress();
        this.usernameStoreOwner = employee.getStoreOwner().getUsername();
    }

  public EmployeeDTO(String username, String password, String email, String phone, String address, Roles roles, byte[] avtUser, Date createdAt, Date updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = roles;
        this.avtUser = avtUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
  }
}
