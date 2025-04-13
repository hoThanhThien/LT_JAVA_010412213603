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

  @ManyToOne
  @JoinColumn(name = "shop_id")
  private LaundryShop shop;

  @OneToMany(mappedBy = "category")
  private List<Service> services;
}
