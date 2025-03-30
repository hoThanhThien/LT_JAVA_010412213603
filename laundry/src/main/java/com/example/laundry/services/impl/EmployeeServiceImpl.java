package com.example.laundry.services.impl;

import com.example.laundry.models.user.Employee;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

//    @Override
//    public void notifyOrderCompleted(Employee employee, Long orderId) {
//        employeeRepository.notifyOrderCompleted(employee, orderId);
//    }
//
//    @Override
//    public void notifyCustomer(Employee employee, Long orderId, String message, String notificationType) {
//        employeeRepository.notifyCustomer(employee, orderId, message, notificationType);
//    }
}
