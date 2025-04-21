package com.example.laundry.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaundryShopDTO {
  private Long id;
  private String name;
  private String description;
  private String address;
  private String openingHours;
  private double averageRating;
  private StoreOwnerSimpleDTO  storeOwner;

  public LaundryShopDTO(Long id, String name, String address, String description, String openingHours) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.description = description;
    this.openingHours = openingHours;
  }

  @Data
  public static class StoreOwnerSimpleDTO {
    private String username;
  }
}
