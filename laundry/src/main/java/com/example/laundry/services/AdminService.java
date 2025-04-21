package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService extends UserService {
    ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO);
    PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size);
    ApiResponse<List<OrderResponse>> getAllOrders(int page, int size);
    ApiResponse<List<OrderResponse>> getOrdersByCustomer(UUID customerId);
    PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size);
    PagedResponse<EmployeeDTO> getAllEmployees(int page, int size);
    PagedResponse<CustomerDTO> getAllCustomers(int page, int size);
}
