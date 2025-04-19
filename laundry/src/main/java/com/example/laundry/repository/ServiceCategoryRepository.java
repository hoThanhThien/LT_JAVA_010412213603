package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {
  boolean existsByName(String name);
  ServiceCategory findByShop(LaundryShop laundryShop);
  boolean existsByNameAndShop(String name, LaundryShop shop);
  boolean existsByNameAndShopAndIdNot(String name, LaundryShop shop, Long id);
}
