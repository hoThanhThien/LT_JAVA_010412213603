package com.example.laundry.services;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;

import java.util.List;

public interface StoreOwnerService extends UserService{
    void addStoreOwner(StoreOwner storeOwner);
    void deleteStoreOwner(StoreOwner storeOwner);
    void removeEmployee(StoreOwner storeOwner, Employee employee);
    void addEmployee(StoreOwner storeOwner, Employee employee);
    void updateEmployee(StoreOwner storeOwner, Employee employee);
    List<Employee> getEmployeesByStoreOwner(StoreOwner storeOwner);;
//    void addService(StoreOwner storeOwner, Service service);
//    void removeService(StoreOwner storeOwner, Service service);
//    void hireEmployee(StoreOwner storeOwner, Employee employee);
//    void fireEmployee(StoreOwner storeOwner, Employee employee);
//    List<Employee> getEmployees(StoreOwner storeOwner);
//    Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate);
}
