package com.example.laundry.dto;

import com.example.laundry.models.user.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOwnerWithEmployeeDTO {
  private UUID id;
  private String username;
  private String phone;
  private String email;
  private String address;
  private String avtUser;
  private Long shopId;
  private String shopName;
  private Roles roles;
  private Date createdAt;
  private Date updatedAt;
  private List<EmployeeDTO> employees;
}
