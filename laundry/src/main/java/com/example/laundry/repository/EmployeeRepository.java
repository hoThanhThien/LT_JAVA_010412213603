package com.example.laundry.repository;

import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmail(String email);
    Employee findByPhone(String phone);
    Employee findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(Integer id);

    List<Employee> findByStoreOwner(StoreOwner storeOwner);
    //    void notifyOrderCompleted(Employee employee, Long orderId);
//    void notifyCustomer(Employee employee, Long orderId, String message, String notificationType);
}
