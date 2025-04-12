<<<<<<< HEAD
package com.example.laundry.services;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.StoreOwner;

import java.util.List;

public interface LaundryShopService {
    LaundryShop findById(Integer id);
    List<LaundryShop> findAll();
    LaundryShop save(LaundryShop laundryShop);
    void deleteById(int id);
    void addService(LaundryShop laundryShop, Service service);
    void removeService(LaundryShop laundryShop, Service service);
    LaundryShop createShopByStoreOwner(LaundryShop laundryShop, StoreOwner storeOwner);
}
=======
package com.example.laundry.services;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;

import java.util.List;

public interface LaundryShopService {
//    LaundryShop findById(Long id);
//    List<LaundryShop> findAll();
//    LaundryShop save(LaundryShop laundryShop);
//    void deleteById(int id);
//    void addService(LaundryShop laundryShop, Service service);
//    void removeService(LaundryShop laundryShop, Service service);
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
