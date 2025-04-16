package com.example.laundry.repository;

import com.example.laundry.models.shop.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service,Long> {
  boolean existsByName(String name);
}
