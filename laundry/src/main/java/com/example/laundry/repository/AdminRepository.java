package com.example.laundry.repository;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Admin findByUsername(String username);
//    void verifyShopDetails(Admin admin, LaundryShop laundryShop);
//    void createStoreOwner(Admin admin, StoreOwner storeOwner);
//    void manageUsers(Admin admin, User user);
//    void sendPlatformNotification(Admin admin, String message);
//    void generatePlatformReport(Admin admin);
}
