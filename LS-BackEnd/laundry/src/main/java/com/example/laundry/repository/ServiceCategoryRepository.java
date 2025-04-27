package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {
  boolean existsByName(String name);
  ServiceCategory findByShop(LaundryShop laundryShop);
  boolean existsByNameAndShop(String name, LaundryShop shop);
  boolean existsByNameAndShopAndIdNot(String name, LaundryShop shop, Long id);
  List<ServiceCategory> findByShopId(Integer shop_id);
  Page<ServiceCategory> findServiceCategoryByShop(LaundryShop laundryShop, Pageable pageable);
}
