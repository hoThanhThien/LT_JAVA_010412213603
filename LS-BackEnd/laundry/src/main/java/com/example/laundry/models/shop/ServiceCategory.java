package com.example.laundry.models.shop;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "service_categories")
public class ServiceCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column
  private String description;

  @Lob
  @Column(name = "image_desc", columnDefinition = "LONGTEXT")
  private String imageDesc;

  @ManyToOne
  @JoinColumn(name = "shop_id")
  private LaundryShop shop;

  @OneToMany(mappedBy = "category")
  private List<Service> services;

  public ServiceCategory(Long id, String name, String description, String imageDesc) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imageDesc = imageDesc;
  }
}
