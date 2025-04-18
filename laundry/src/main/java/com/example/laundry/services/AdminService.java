package com.example.laundry.services;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

public interface AdminService extends UserService {
    ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO);
    PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size);
}
