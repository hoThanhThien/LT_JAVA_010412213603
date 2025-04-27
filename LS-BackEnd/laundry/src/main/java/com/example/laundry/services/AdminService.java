package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService extends UserService {
    ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<StoreOwnerDTO> updateStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO);
    PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size);
    PagedResponse<StoreOwnerWithEmployeeDTO> getAllEmployeesBelongToStoreOwner(int page, int size);
    PagedResponse<OrderResponse> getAllOrders(int page, int size);
    PagedResponse<OrderResponse> getOrdersByCustomer(UUID customerId, int page, int size);
    PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size);
    PagedResponse<CustomerDTO> getAllCustomers(int page, int size);
}
