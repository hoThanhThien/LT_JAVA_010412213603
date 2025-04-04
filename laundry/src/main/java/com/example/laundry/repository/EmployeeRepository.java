package com.example.laundry.repository;

import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Employee findByEmail(String email);
    Employee findByPhone(String phone);
    Employee findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(@NonNull UUID id);

    List<Employee> findByStoreOwner(StoreOwner storeOwner);
    //    void notifyOrderCompleted(Employee employee, Long orderId);
//    void notifyCustomer(Employee employee, Long orderId, String message, String notificationType);
}
