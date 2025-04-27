package com.example.laundry.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithServiceDTO {
  private Long id;
  private String name;
  private String image_desc;
  private String description;
  private List<ServiceDTO> services;
}
