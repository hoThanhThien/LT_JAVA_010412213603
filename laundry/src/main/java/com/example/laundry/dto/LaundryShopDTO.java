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

  @Data
  @Getter
  @Setter
  public static class StoreOwnerSimpleDTO {
    private String username;
  }
}
