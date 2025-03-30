package com.example.laundry.repository;

import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
//    void notifyOrderCompleted(Employee employee, Long orderId);
//    void notifyCustomer(Employee employee, Long orderId, String message, String notificationType);
}
