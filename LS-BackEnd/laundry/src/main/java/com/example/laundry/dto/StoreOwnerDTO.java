package com.example.laundry.dto;

import com.example.laundry.models.user.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreOwnerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Roles role;
    private String avtUser;
    private Date createdAt;
    private Date updatedAt;

    public StoreOwnerDTO(String username, String password, String email, String phone, String address, Roles role, String avtUser, Date createdAt, Date updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.avtUser = avtUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}


