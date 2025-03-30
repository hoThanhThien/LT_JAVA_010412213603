package com.example.laundry.services;

import com.example.laundry.models.user.Employee;

public interface EmployeeService extends UserService {
    void addEmployee(Employee employee);
    void deleteEmployee(Employee employee);
    void updateEmployee(Employee employee);
//    void notifyOrderCompleted(Employee employee, Long orderId);
//    void notifyCustomer(Employee employee, Long orderId, String message, String notificationType);
}
