package com.example.laundry.services.impl;

import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.services.LaundryShopService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LaundryShopServiceImpl implements LaundryShopService {
    @Autowired
    private LaundryShopRepository laundryShopRepository;

    @Override
    public LaundryShop findById(Integer id) {
        return laundryShopRepository.findById(id).orElse(null);
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
    public LaundryShop createShopByStoreOwner(LaundryShop laundryShop, StoreOwner storeOwner) {
      return laundryShopRepository.save(laundryShop);
    }

    @Override
    public ApiResponse<List<LaundryShopDTO>> getAllShop() {
        List<LaundryShop> laundryShops = laundryShopRepository.findAll();

        List<LaundryShopDTO> laundryShopDTOs = laundryShops.stream()
                .map(shop -> new LaundryShopDTO(
                        shop.getId(),
                        shop.getName(),
                        shop.getAddress(),
                        shop.getDescription(),
                        shop.getOpeningHours()
                ))
                .toList();


        return new ApiResponse<>("Danh sách các cửa hàng", laundryShopDTOs);
    }
}
