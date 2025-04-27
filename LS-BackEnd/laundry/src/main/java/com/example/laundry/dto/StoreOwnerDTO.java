package com.example.laundry.dto;

import com.example.laundry.models.user.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreOwnerDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Roles role;
    private String avtUser;
    private Date createdAt;
    private Date updatedAt;
}


