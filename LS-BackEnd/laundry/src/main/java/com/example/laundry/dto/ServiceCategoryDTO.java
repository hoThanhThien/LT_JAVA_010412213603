package com.example.laundry.dto;

import com.example.laundry.models.shop.LaundryShop;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCategoryDTO {
  private Long id;
  private String name;
  private String imageDesc;
  private String description;
}
