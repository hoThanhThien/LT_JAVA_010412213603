package com.example.laundry.repository;

import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner,Integer> {
    StoreOwner findByEmail(String email);
    StoreOwner findByPhone(String phone);
    StoreOwner findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(Integer id);
//    void addService(StoreOwner storeOwner, Service service);
//    void removeService(StoreOwner storeOwner, Service service);
//    void hireEmployee(StoreOwner storeOwner, Employee employee);
//    void fireEmployee(StoreOwner storeOwner, Employee employee);
//    List<Employee> getEmployees(StoreOwner storeOwner);
//    Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate);
}
