package com.example.laundry.services.impl;

import com.example.laundry.models.report.Report;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.models.user.User;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.StoreOwnerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class StoreOwnerServiceImpl implements StoreOwnerService {
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void addStoreOwner(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    @Override
    public void deleteStoreOwner(StoreOwner storeOwner) {
        storeOwnerRepository.delete(storeOwner);
    }

    @Override
    public void addEmployee(StoreOwner storeOwner, Employee employee) {
        // Đảm bảo Employee biết StoreOwner nào tạo ra nó
        employee.setStoreOwner(storeOwner);
        employeeRepository.save(employee);
    }

    @Override
    public void removeEmployee(StoreOwner storeOwner, Employee employee) {
        // Chỉ xóa Employee nếu nó thuộc về StoreOwner hiện tại
        if (employee.getStoreOwner() != null && employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            employeeRepository.delete(employee);
        }
    }

    @Override
    public void updateEmployee(StoreOwner storeOwner, Employee employee) {
        // Chỉ cập nhật Employee nếu nó thuộc về StoreOwner hiện tại
        if (employee.getStoreOwner() != null && employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            employeeRepository.save(employee);
        }
    }

    @Override
    public List<Employee> getEmployeesByStoreOwner(StoreOwner storeOwner) {
        return employeeRepository.findByStoreOwner(storeOwner);
    }
}
