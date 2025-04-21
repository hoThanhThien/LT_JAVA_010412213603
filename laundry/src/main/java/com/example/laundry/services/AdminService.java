package com.example.laundry.services;

import com.example.laundry.dto.OrderResponse;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

import java.util.UUID;

public interface AdminService extends UserService {
    ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO);
    PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size);
    //PagedResponse<OrderResponse> getAllOrders();
    //PagedResponse<OrderResponse> getOrdersByCustomer(UUID customerId);
    PagedResponse<OrderResponse> getAllOrders(int page, int size);
    PagedResponse<OrderResponse> getOrdersByCustomer(UUID customerId, int page, int size);
    PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size);

}
