package com.example.laundry.repository;

import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner,Integer> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
//    void addService(StoreOwner storeOwner, Service service);
//    void removeService(StoreOwner storeOwner, Service service);
//    void hireEmployee(StoreOwner storeOwner, Employee employee);
//    void fireEmployee(StoreOwner storeOwner, Employee employee);
//    List<Employee> getEmployees(StoreOwner storeOwner);
//    Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate);
}
