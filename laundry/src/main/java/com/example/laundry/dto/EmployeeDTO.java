package com.example.laundry.dto;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public EmployeeDTO(String username, String password, String email, String phone, String address, Roles roles, byte[] avtUser, Date createdAt, Date updatedAt, Employee employee) {
          this.username = username;
          this.password = password;
          this.email = email;
          this.phone = phone;
          this.address = address;
          this.role = roles;
          this.avtUser = avtUser;
          this.createdAt = createdAt;
          this.updatedAt = updatedAt;
          this.usernameStoreOwner = employee.getStoreOwner().getUsername();
    }

    public EmployeeDTO(String username, String phone, String email, String address, byte[] avtUser, Roles roles, LaundryShop shop, Date createdAt, Date updatedAt) {
      this.username = username;
      this.phone = phone;
      this.email = email;
      this.address = address;
      this.role = roles;
      this.avtUser = avtUser;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
}
