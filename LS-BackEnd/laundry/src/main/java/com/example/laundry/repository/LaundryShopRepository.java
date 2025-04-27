package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LaundryShopRepository extends JpaRepository<LaundryShop, Integer> {
  LaundryShop findByStoreOwner(StoreOwner storeOwner);
  boolean existsByStoreOwner(StoreOwner storeOwner);
  boolean existsByName(String name);
  Optional<LaundryShop> findById(@NonNull Integer id);
  Optional<LaundryShop> findByStoreOwnerId(UUID storeOwner_Id);
}
