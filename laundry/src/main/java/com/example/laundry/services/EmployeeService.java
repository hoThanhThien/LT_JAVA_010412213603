package com.example.laundry.services;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Employee;
import com.example.laundry.utils.ApiResponse;

public interface EmployeeService extends UserService {
    void addEmployee(Employee employee);
    void deleteEmployee(Employee employee);
    void updateEmployee(Employee employee);
    ApiResponse<Order> updateOrderStatus(Integer orderId, OrderStatus status, Employee employee);
}
