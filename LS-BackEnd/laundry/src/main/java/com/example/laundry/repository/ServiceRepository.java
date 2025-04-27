package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service,Long> {
  boolean existsByName(String name);
  boolean existsByNameAndCategory_Shop(String name, LaundryShop shop);
  boolean existsByNameAndCategory_ShopAndIdNot(String name, LaundryShop shop, Long id);
  List<Service> findByCategoryId(Integer id);
  List<Service> findByCategoryIdAndCategoryShopId(Integer categoryId, Integer shopId);
  List<Service> findByCategory(ServiceCategory category);
  boolean existsByNameAndCategory(String name, ServiceCategory serviceCategory);
}
