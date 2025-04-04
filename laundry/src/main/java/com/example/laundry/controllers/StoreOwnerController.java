package com.example.laundry.controllers;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
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
    public ResponseEntity<ApiResponse<Employee>> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>( "Không tìm thấy thông tin StoreOwner", null));
        }

        // Kiểm tra dữ liệu
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Mật khẩu không được để trống", null));
        }

        // Kiểm tra password
        if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Mật khẩu không hợp lệ!!!", null));
        }

        // Kiểm tra email
        if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Email không hợp lệ!!!", null));
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Email đã tồn tại!!!", null));
        }

        // Kiểm tra phone
        if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Số điện thoại phải có 10 chữ số!!!", null));
        }

        if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Số điện thoại đã tồn tại!!!", null));
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

        return ResponseEntity
                .ok(new ApiResponse<>("Tạo tài khoản nhân viên thành công", employee));
    }

    @DeleteMapping("/employee/delete")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Lấy thông tin StoreOwner hiện tại
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }

        // Kiểm tra xem tồn tại hay không
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Không đủ thông tin để xóa!!!", null));
        }

        // Tìm Employee theo các thông tin cung cấp
        Employee employee = findEmployeeByInfo(employeeDTO);

        if (employee == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Không tìm thấy nhân viên với thông tin đã cung cấp!!!", null));
        }

        // Kiểm tra Employee có thuộc về StoreOwner hiện tại không
        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Bạn không có quyền xóa nhân viên này", null));
        }

        storeOwnerService.removeEmployee(storeOwner, employee);

        return ResponseEntity.ok(new ApiResponse<>("Đã xóa nhân viên thành công", null));
    }

    @PutMapping("/employee/update")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }

        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>( "Cần cung cấp ít nhất một thông tin để tìm Employee.", null));
        }

        Employee employee = findEmployeeByInfo(employeeDTO);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Không tìm thấy Employee với thông tin đã cung cấp.", null));
        }

        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Bạn không có quyền cập nhật Employee này", null));
        }

        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().equals(employee.getEmail())) {
            if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email không hợp lệ!!!", null));
            }
            if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email đã tồn tại!!!", null));
            }
            employee.setEmail(employeeDTO.getEmail());
        }

        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().equals(employee.getPhone())) {
            if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Phone phải có 10 chữ số!!!", null));
            }
            if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Phone đã tồn tại!!!", null));
            }
            employee.setPhone(employeeDTO.getPhone());
        }

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty() &&
                !employeeDTO.getUsername().equals(employee.getUsername())) {
            employee.setUsername(employeeDTO.getUsername());
        }

        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Password không hợp lệ!!!", null));
            }
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }

        storeOwnerService.updateEmployee(storeOwner, employee);

        return ResponseEntity
                .ok(new ApiResponse<>("Cập nhật Employee thành công!", employee));
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        StoreOwner storeOwner = getCurrentStoreOwner();
        if (storeOwner == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Không tìm thấy thông tin StoreOwner", null));
        }

        List<Employee> employees = storeOwnerService.getEmployeesByStoreOwner(storeOwner);
        return ResponseEntity.ok(new ApiResponse<>("Danh sách nhân viên", employees));
    }

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