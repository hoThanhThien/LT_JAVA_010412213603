package com.example.laundry.services;

import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.utils.ApiResponse;

public interface AdminService extends UserService {
    ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO);
    ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO);
//    void createStoreOwner(Admin admin, StoreOwner storeOwner);
//    void verifyShopDetails(Admin admin, LaundryShop laundryShop);
//    void manageUsers(Admin admin, User user);
//    void sendPlatformNotification(Admin admin, String message);
//    void generatePlatformReport(Admin admin);
}
