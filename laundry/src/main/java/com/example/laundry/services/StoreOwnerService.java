package com.example.laundry.services;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.utils.ApiResponse;

public interface StoreOwnerService extends UserService {
    void addStoreOwner(StoreOwner storeOwner);
    void deleteStoreOwner(StoreOwner storeOwner);
    ApiResponse<Employee> createEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<String> deleteEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<Employee> updateEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO);
    ApiResponse<LaundryShopDTO> createShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    LaundryShop getLaundryShopByStoreOwner(StoreOwner storeOwner);
    ApiResponse<LaundryShopDTO> updateLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    ApiResponse<String> deleteLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO);
    ApiResponse<ServiceCategoryDTO> createServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<ServiceCategoryDTO>  updateServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<Integer>  deleteServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO);
    ApiResponse<ServiceDTO> createService(StoreOwner storeOwner, ServiceDTO serviceDTO);
    ApiResponse<ServiceDTO>  updateService(StoreOwner storeOwner, ServiceDTO serviceDTO);
    ApiResponse<Integer>  deleteService(StoreOwner storeOwner, ServiceDTO serviceDTO);
}