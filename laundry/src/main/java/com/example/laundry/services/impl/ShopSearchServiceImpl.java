package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.ShopSearchRepository;
import com.example.laundry.services.ShopSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSearchServiceImpl implements ShopSearchService {
    @Autowired
    private ShopSearchRepository shopSearchRepository;

//    @Override
//    public List<LaundryShop> findShops(String location, String service) {
//        return shopSearchRepository.findShops(location, service);
//    }
}
