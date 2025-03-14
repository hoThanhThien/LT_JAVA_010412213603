package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.services.LaundryShopService;

import java.util.List;

@org.springframework.stereotype.Service
public class LaundryShopServiceimpl implements LaundryShopService {

    @Override
    public LaundryShop findById(int id) {
        return null;
    }

    @Override
    public List<LaundryShop> findAll() {
        return List.of();
    }

    @Override
    public LaundryShop save(LaundryShop laundryShop) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void addService(LaundryShop laundryShop, Service service) {

    }

    @Override
    public void removeService(LaundryShop laundryShop, Service service) {

    }
}
