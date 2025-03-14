package com.example.laundry.services.impl;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.AdminRepository;
import com.example.laundry.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void createStoreOwner(Admin admin, StoreOwner storeOwner) {
        adminRepository.createStoreOwner(admin, storeOwner);
    }

    @Override
    public void verifyShopDetails(Admin admin, LaundryShop laundryShop) {
        adminRepository.verifyShopDetails(admin, laundryShop);
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
    public User findUserById(long id) {
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
    public void deleteById(long id) {

    }
}
