package com.example.laundry.services.impl;

import com.example.laundry.models.user.Employee;
import com.example.laundry.services.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public void notifyOrderCompleted(Employee employee, Long orderId) {

    }

    @Override
    public void notifyCustomer(Employee employee, Long orderId, String message, String notificationType) {

    }
}
