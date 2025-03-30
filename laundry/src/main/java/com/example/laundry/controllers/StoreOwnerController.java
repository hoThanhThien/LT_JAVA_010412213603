package com.example.laundry.controllers;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storeowner")
public class StoreOwnerController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/employee/create")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        //Kiểm tra dữ liệu
        if(employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password không được để trống");
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return ResponseEntity.badRequest().body("Password không hợp lệ!!!");
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email không hợp lệ!!!");
        }

        if(employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!!!");
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone phải có 10 chữ số!!!");
        }

        if(employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone đã tồn tại!!!");
        }

        //Mã hóa password trước khi lưu xuống db
        String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        Employee employee = new Employee(
                employeeDTO.getUsername(),
                encodedPassword,
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getAddress(),
                Roles.Employee
        );

        employeeService.addEmployee(employee);

        return ResponseEntity.ok("Tạo tài khoản employee thành công");
    }
}
