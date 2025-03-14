package com.example.laundry.services;

import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;

import java.util.List;

public interface StoreOwnerService extends UserService{
    void addService(StoreOwner storeOwner, Service service);
    void removeService(StoreOwner storeOwner, Service service);
    void hireEmployee(StoreOwner storeOwner, Employee employee);
    void fireEmployee(StoreOwner storeOwner, Employee employee);
    List<Employee> getEmployees(StoreOwner storeOwner);
    Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate);
}
