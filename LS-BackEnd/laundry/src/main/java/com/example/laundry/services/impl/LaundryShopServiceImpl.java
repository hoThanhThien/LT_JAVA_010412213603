package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.services.LaundryShopService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public PagedResponse<LaundryShopDTO> getAllShops(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LaundryShop> shopPage = laundryShopRepository.findAll(pageable);

        List<LaundryShopDTO> dtos = shopPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Meta meta = new Meta(
                shopPage.getNumber() + 1,
                shopPage.getSize(),
                shopPage.getTotalElements(),
                shopPage.getTotalPages()
        );

        PagedData<LaundryShopDTO> pagedData = new PagedData<>(meta, dtos);
        return new PagedResponse<>("Lấy danh sách tất cả cửa hàng thành công", pagedData);
    }

    @Override
    public ApiResponse<List<LaundryShopDTO>> getAllShop() {
        List<LaundryShop> laundryShops = laundryShopRepository.findAll();

        List<LaundryShopDTO> laundryShopDTOs = laundryShops.stream()
                .map(this::convertToDTO)
                .toList();

        return new ApiResponse<>("Danh sách các cửa hàng", laundryShopDTOs);
    }

    private LaundryShopDTO convertToDTO(LaundryShop laundryShop) {
        LaundryShopDTO dto = new LaundryShopDTO();
        dto.setId(laundryShop.getId());
        dto.setName(laundryShop.getName());
        dto.setAddress(laundryShop.getAddress());
        dto.setOpeningHours(laundryShop.getOpeningHours());
        dto.setAverageRating(laundryShop.getAverageRating());
        dto.setCreatedAt(laundryShop.getCreatedAt());
        LaundryShopDTO.StoreOwnerSimpleDTO storeOwnerSimpleDTO = new LaundryShopDTO.StoreOwnerSimpleDTO();
        storeOwnerSimpleDTO.setUsername(laundryShop.getStoreOwner().getUsername());
        dto.setStoreOwner(storeOwnerSimpleDTO);

        return dto;
    }
}
