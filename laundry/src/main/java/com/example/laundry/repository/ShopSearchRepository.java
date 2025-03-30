package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopSearchRepository extends JpaRepository<LaundryShop,Integer> {
//    List<LaundryShop> findShops(String location, String service);
}
