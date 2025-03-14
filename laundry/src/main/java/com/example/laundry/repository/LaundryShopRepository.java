package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface LaundryShopRepository extends JpaRepository<LaundryShop, Integer> {
    LaundryShop findById(long id);
    List<LaundryShop> findAll();
    LaundryShop save(LaundryShop laundryShop);
    void deleteById(long id);
    void removeService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service);
    void addService(LaundryShop laundryShop, com.example.laundry.models.shop.Service service);
}
