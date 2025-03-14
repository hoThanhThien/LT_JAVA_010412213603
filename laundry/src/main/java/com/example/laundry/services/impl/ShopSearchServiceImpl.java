package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.services.ShopSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSearchServiceImpl implements ShopSearchService {
    @Override
    public List<LaundryShop> findShops(String location, String service) {
        return List.of();
    }
}
