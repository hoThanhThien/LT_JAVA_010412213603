package com.example.laundry.services;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

import java.util.List;

public interface LaundryShopService {
    LaundryShop findById(Integer id);
    LaundryShop save(LaundryShop laundryShop);
    void deleteById(int id);
    LaundryShop createShopByStoreOwner(LaundryShop laundryShop, StoreOwner storeOwner);
    PagedResponse<LaundryShopDTO> getAllShops(int page, int size);
    ApiResponse<List<LaundryShopDTO>> getAllShop();
}
