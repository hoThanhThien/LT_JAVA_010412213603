package com.example.laundry.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
