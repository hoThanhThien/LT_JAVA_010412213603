package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
//import com.example.laundry.models.shop.Service;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.services.LaundryShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaundryShopServiceImpl implements LaundryShopService {
    @Autowired
    private LaundryShopRepository laundryShopRepository;

    @Override
    public LaundryShop findById(Long id) {
        return laundryShopRepository.findById(id);
    }

    @Override
    public List<LaundryShop> findAll() {
        return laundryShopRepository.findAll();
    }

    @Override
    public LaundryShop save(LaundryShop laundryShop) {
        return laundryShopRepository.save(laundryShop);
    }

    @Override
    public void deleteById(int id) {
        laundryShopRepository.deleteById(id);
    }

    @Override
    public void addService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service) {
        laundryShopRepository.addService(laundryShop, service);
    }

    @Override
    public void removeService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service) {
        laundryShopRepository.removeService(laundryShop, service);
    }
}
