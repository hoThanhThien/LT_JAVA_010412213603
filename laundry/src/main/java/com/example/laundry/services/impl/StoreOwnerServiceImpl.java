package com.example.laundry.services.impl;

import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.services.StoreOwnerService;

import java.util.List;

@org.springframework.stereotype.Service
public class StoreOwnerServiceImpl implements StoreOwnerService {
    @Override
    public void addService(StoreOwner storeOwner, Service service) {

    }

    @Override
    public void removeService(StoreOwner storeOwner, Service service) {

    }

    @Override
    public void hireEmployee(StoreOwner storeOwner, Employee employee) {

    }

    @Override
    public void fireEmployee(StoreOwner storeOwner, Employee employee) {

    }

    @Override
    public List<Employee> getEmployees(StoreOwner storeOwner) {
        return List.of();
    }

    @Override
    public Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate) {
        return null;
    }

    @Override
    public User findUserById(long id) {
        return StoreOwnerService.super.findUserById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return StoreOwnerService.super.findAllUsers();
    }

    @Override
    public User save(User user) {
        return StoreOwnerService.super.save(user);
    }

    @Override
    public void deleteById(long id) {
        StoreOwnerService.super.deleteById(id);
    }
}
