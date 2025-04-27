package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

import java.util.List;

public interface StoreOwnerService extends UserService {
    void addStoreOwner(StoreOwner storeOwner);
    void deleteStoreOwner(StoreOwner storeOwner);
    ApiResponse<EmployeeDTO> createEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<String> deleteEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<EmployeeDTO> updateEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<LaundryShopDTO> createShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    ApiResponse<LaundryShopDTO> getLaundryShopByStoreOwner(StoreOwner storeOwner);
    ApiResponse<LaundryShopDTO> updateLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    ApiResponse<String> deleteLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    ApiResponse<ServiceCategoryDTO> createServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<ServiceCategoryDTO>  updateServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<Integer>  deleteServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<ServiceDTO> createService(StoreOwner storeOwner, ServiceDTO serviceDTO);
    ApiResponse<ServiceDTO>  updateService(StoreOwner storeOwner, ServiceDTO serviceDTO);
    ApiResponse<Integer>  deleteService(StoreOwner storeOwner, ServiceDTO serviceDTO);
    PagedResponse<CategoryWithServiceDTO> getAllCategoriesWithServicesByStore(StoreOwner storeOwner, int page, int size);
    PagedResponse<OrderResponse> getAllOrdersByStoreOwner(StoreOwner storeOwner, int page, int size);
}