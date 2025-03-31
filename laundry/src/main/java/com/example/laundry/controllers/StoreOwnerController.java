package com.example.laundry.controllers;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storeowner")
public class StoreOwnerController {
    @Autowired
    private StoreOwnerService storeOwnerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    // Lấy store owner hiện tại
    private StoreOwner getCurrentStoreOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return storeOwnerRepository.findByUsername(currentUsername);
    }

    @PostMapping("/employee/create")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy thông tin StoreOwner");
        }

        // Kiểm tra dữ liệu
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password không được để trống");
        }

        // Kiểm tra password
        if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return ResponseEntity.badRequest().body("Password không hợp lệ!!!");
        }

        // Kiểm tra email
        if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email không hợp lệ!!!");
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!!!");
        }

        // Kiểm tra phone
        if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone phải có 10 chữ số!!!");
        }

        if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone đã tồn tại!!!");
        }

        // Mã hóa password trước khi lưu xuống db
        String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        // Tạo đối tượng Employee và thiết lập mối quan hệ với StoreOwner
        Employee employee = new Employee(
                employeeDTO.getUsername(),
                encodedPassword,
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getAddress(),
                Roles.Employee,
                storeOwner
        );

        // Sử dụng service để thêm employee với quan hệ storeOwner
        storeOwnerService.addEmployee(storeOwner, employee);

        return ResponseEntity.ok("Tạo tài khoản employee thành công");
    }

    @DeleteMapping("/employee/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy thông tin StoreOwner");
        }

        // Kiểm tra xem tồn tại hay không
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ResponseEntity.badRequest().body("Không đủ thông tin để xóa!!!");
        }

        // Tìm Employee theo các thông tin cung cấp
        Employee employee = findEmployeeByInfo(employeeDTO);

        if (employee == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy Employee với thông tin đã cung cấp");
        }

        // Kiểm tra Employee có thuộc về StoreOwner hiện tại không
        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa Employee này");
        }

        storeOwnerService.removeEmployee(storeOwner, employee);

        return ResponseEntity.ok("Đã xóa Employee thành công!!!");
    }

    @PutMapping("/employee/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy thông tin StoreOwner");
        }

        // Kiểm tra nếu không có đủ thông tin để xác định Employee
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ResponseEntity.badRequest().body("Cần cung cấp ít nhất một thông tin để tìm Employee.");
        }

        // Tìm Employee theo các thông tin cung cấp
        Employee employee = findEmployeeByInfo(employeeDTO);

        // Kiểm tra nếu không tìm thấy Employee
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Employee với thông tin đã cung cấp.");
        }

        // Kiểm tra Employee có thuộc về StoreOwner hiện tại không
        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền cập nhật Employee này");
        }

        // Cập nhật email
        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().equals(employee.getEmail())) {
            // Kiểm tra email hợp lệ
            if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email không hợp lệ!!!");
            }

            // Kiểm tra email đã tồn tại
            if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email đã tồn tại!!!");
            }

            employee.setEmail(employeeDTO.getEmail());
        }

        // Cập nhật phone
        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().equals(employee.getPhone())) {
            // Kiểm tra phone hợp lệ
            if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
                return ResponseEntity.badRequest().body("Phone phải có 10 chữ số!!!");
            }

            // Kiểm tra phone đã tồn tại
            if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
                return ResponseEntity.badRequest().body("Phone đã tồn tại!!!");
            }

            employee.setPhone(employeeDTO.getPhone());
        }

        // Cập nhật username nếu có
        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty() &&
                !employeeDTO.getUsername().equals(employee.getUsername())) {
            employee.setUsername(employeeDTO.getUsername());
        }

        // Cập nhật password nếu có
        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            // Kiểm tra password hợp lệ
            if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
                return ResponseEntity.badRequest().body("Password không hợp lệ!!!");
            }

            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        // Cập nhật địa chỉ nếu có
        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }

        // Sử dụng service để cập nhật employee
        storeOwnerService.updateEmployee(storeOwner, employee);

        return ResponseEntity.ok("Cập nhật Employee thành công!");
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<?> getAllEmployees() {
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy thông tin StoreOwner");
        }

        List<Employee> employees = storeOwnerService.getEmployeesByStoreOwner(storeOwner);
        return ResponseEntity.ok(employees);
    }

    // Helper method để tìm Employee từ EmployeeDTO
    private Employee findEmployeeByInfo(EmployeeDTO employeeDTO) {
        Employee employee = null;
        if (employeeDTO.getEmail() != null) {
            employee = employeeRepository.findByEmail(employeeDTO.getEmail());
        }
        if (employee == null && employeeDTO.getPhone() != null) {
            employee = employeeRepository.findByPhone(employeeDTO.getPhone());
        }
        if (employee == null && employeeDTO.getUsername() != null) {
            employee = employeeRepository.findByUsername(employeeDTO.getUsername());
        }
        return employee;
    }
}