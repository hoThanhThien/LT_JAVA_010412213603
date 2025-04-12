<<<<<<< HEAD
package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaundryShopRepository extends JpaRepository<LaundryShop, Integer> {
  LaundryShop findByStoreOwner(StoreOwner storeOwner);
  boolean existsByStoreOwner(StoreOwner storeOwner);
  boolean existsByName(String name);
}
=======
package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaundryShopRepository extends JpaRepository<LaundryShop, Integer> {
//    LaundryShop findById(Long id);
//    @NonNull
//    List<LaundryShop> findAll();
//    @NonNull
//    LaundryShop save(@NonNull LaundryShop laundryShop);
//    void deleteById(Long id);
//    void removeService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service);
//    void addService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service);
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
