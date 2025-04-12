<<<<<<< HEAD
package com.example.laundry.repository;

import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, UUID> {
    StoreOwner findByEmail(String email);
    StoreOwner findByPhone(String phone);
    StoreOwner findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(@NonNull UUID id);
}
=======
package com.example.laundry.repository;

import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, UUID> {
    StoreOwner findByEmail(String email);
    StoreOwner findByPhone(String phone);
    StoreOwner findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(@NonNull UUID id);
//    void addService(StoreOwner storeOwner, Service service);
//    void removeService(StoreOwner storeOwner, Service service);
//    void hireEmployee(StoreOwner storeOwner, Employee employee);
//    void fireEmployee(StoreOwner storeOwner, Employee employee);
//    List<Employee> getEmployees(StoreOwner storeOwner);
//    Report generateFinancialReport(StoreOwner storeOwner, String startDate, String endDate);
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
