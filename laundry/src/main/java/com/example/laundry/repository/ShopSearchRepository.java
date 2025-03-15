package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopSearchRepository {
    List<LaundryShop> findShops(String location, String service);
}
