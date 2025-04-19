package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service,Long> {
  boolean existsByName(String name);
  boolean existsByNameAndCategory_Shop(String name, LaundryShop shop);
  boolean existsByNameAndCategory_ShopAndIdNot(String name, LaundryShop shop, Long id);
}
