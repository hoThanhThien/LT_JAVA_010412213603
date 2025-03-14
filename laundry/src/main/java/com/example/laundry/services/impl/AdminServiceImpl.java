package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.services.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    @Override
    public void createStoreOwner(Admin admin, StoreOwner storeOwner) {

    }

    @Override
    public void verifyShopDetails(Admin admin, LaundryShop laundryShop) {

    }

    @Override
    public void manageUsers(Admin admin, User user) {

    }

    @Override
    public void sendPlatformNotification(Admin admin, String message) {

    }

    @Override
    public void generatePlatformReport(Admin admin) {

    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
