package com.example.laundry.services.impl;

import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.StoreOwnerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class StoreOwnerServiceImpl implements StoreOwnerService {
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void addStoreOwner(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    @Override
    public void createStoreOwner(StoreOwner storeOwner, Employee employee) {
        employeeRepository.save(employee);
    }

//    @Override
//    public void addService(StoreOwner storeOwner, Service service) {
//        storeOwnerRepository.addService(storeOwner, service);
//    }
//
//    @Override
//    public void removeService(StoreOwner storeOwner, Service service) {
//        storeOwnerRepository.removeService(storeOwner, service);
//    }
//
//    @Override
//    public void hireEmployee(StoreOwner storeOwner, Employee employee) {
//        storeOwnerRepository.hireEmployee(storeOwner, employee);
//    }
//
//    @Override
//    public void fireEmployee(StoreOwner storeOwner, Employee employee) {
//        storeOwnerRepository.fireEmployee(storeOwner, employee);
//    }
//
//    @Override
//    public List<Employee> getEmployees(StoreOwner storeOwner) {
//        storeOwnerRepository.getEmployees(storeOwner);
//        return List.of();
//    }
//
//    @Override
//    public Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate) {
//        storeOwnerRepository.generateFinancialReport(storeOwner, startDate, endDate);
//        return null;
//    }

//    @Override
//    public User findUserById(Long id) {
//        return StoreOwnerService.super.findUserById(id);
//    }
//
//    @Override
//    public List<User> findAllUsers() {
//        return StoreOwnerService.super.findAllUsers();
//    }
//
//    @Override
//    public User save(User user) {
//        return StoreOwnerService.super.save(user);
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        StoreOwnerService.super.deleteById(id);
//    }
}
