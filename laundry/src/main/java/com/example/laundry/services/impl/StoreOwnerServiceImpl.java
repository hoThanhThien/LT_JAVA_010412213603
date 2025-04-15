package com.example.laundry.services.impl;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StoreOwnerServiceImpl implements StoreOwnerService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LaundryShopRepository laundryShopRepository;

    private StoreOwner getCurrentStoreOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return storeOwnerRepository.findByUsername(currentUsername);
    }

    @Override
    public void addStoreOwner(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    @Override
    public void deleteStoreOwner(StoreOwner storeOwner) {
        storeOwnerRepository.delete(storeOwner);
    }

    @Override
    public ApiResponse<Employee> createEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return ApiResponse.error("Mật khẩu không được để trống");
        }

        if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return ApiResponse.error("Mật khẩu không hợp lệ!!!");
        }

        if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return ApiResponse.error("Email không hợp lệ!!!");
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return ApiResponse.error("Email đã tồn tại!!!");
        }

        if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return ApiResponse.error("Số điện thoại phải có 10 chữ số!!!");
        }

        if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return ApiResponse.error("Số điện thoại đã tồn tại!!!");
        }

        LaundryShop laundryShop = laundryShopRepository.findByStoreOwner(storeOwner);
        if (laundryShop == null) {
            return ApiResponse.error("StoreOwner chưa có shop, không thể tạo nhân viên");
        }

        String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        Employee employee = new Employee(
                employeeDTO.getUsername(),
                encodedPassword,
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getAddress(),
                Roles.Employee,
                storeOwner
        );
        employee.setShop(laundryShop);

        employeeService.addEmployee(employee);
        return ApiResponse.success(employee, "Tạo tài khoản nhân viên thành công");
    }

    @Override
    public ApiResponse<String> deleteEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ApiResponse.error("Không đủ thông tin để xóa!!!");
        }

        Employee employee = findEmployeeByInfo(employeeDTO);
        if (employee == null) {
            return ApiResponse.error("Không tìm thấy nhân viên với thông tin đã cung cấp!!!");
        }

        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ApiResponse.error("Bạn không có quyền xóa nhân viên này");
        }

        employeeService.deleteEmployee(employee);
        return ApiResponse.success("Đã xóa nhân viên thành công");
    }

    @Override
    public ApiResponse<Employee> updateEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return ApiResponse.error("Cần cung cấp ít nhất một thông tin để tìm Employee.");
        }

        Employee employee = findEmployeeByInfo(employeeDTO);
        if (employee == null) {
            return ApiResponse.error("Không tìm thấy Employee với thông tin đã cung cấp.");
        }

        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return ApiResponse.error("Bạn không có quyền cập nhật Employee này");
        }

        // Update logic remains the same
        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().equals(employee.getEmail())) {
            if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
                return ApiResponse.error("Email không hợp lệ!!!");
            }
            if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                return ApiResponse.error("Email đã tồn tại!!!");
            }
            employee.setEmail(employeeDTO.getEmail());
        }

        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().equals(employee.getPhone())) {
            if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
                return ApiResponse.error("Phone phải có 10 chữ số!!!");
            }
            if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
                return ApiResponse.error("Phone đã tồn tại!!!");
            }
            employee.setPhone(employeeDTO.getPhone());
        }

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty() &&
                !employeeDTO.getUsername().equals(employee.getUsername())) {
            employee.setUsername(employeeDTO.getUsername());
        }

        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
                return ApiResponse.error("Password không hợp lệ!!!");
            }
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }

        employeeService.updateEmployee(employee);
        return ApiResponse.success(employee, "Cập nhật Employee thành công!");
    }

    @Override
    public ApiResponse<LaundryShopDTO> createShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        if(laundryShopRepository.existsByStoreOwner(storeOwner)) {
            return ApiResponse.error("Bạn đã có cửa hàng rồi, không được tạo thêm");
        }

        if(laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return ApiResponse.error("Tên cửa hàng đã tồn tại vui lòng chọn tên khác!!!");
        }

        if(laundryShopDTO.getName() == null || laundryShopDTO.getName().isEmpty()) {
            return ApiResponse.error("Tên cửa hàng không được để trống");
        }

        if(laundryShopDTO.getAddress() == null || laundryShopDTO.getAddress().isEmpty()) {
            return ApiResponse.error("Địa chỉ cửa hàng không được để trống");
        }

        LaundryShop laundryShop = new LaundryShop(
                laundryShopDTO.getId(),
                laundryShopDTO.getName(),
                laundryShopDTO.getAddress(),
                laundryShopDTO.getOpeningHours(),
                laundryShopDTO.getDescription(),
                laundryShopDTO.getAverageRating(),
                storeOwner
        );

        LaundryShop createdShop = laundryShopRepository.save(laundryShop);
        LaundryShopDTO responseDTO = convertToResponseDTO(createdShop);

        return ApiResponse.success(responseDTO, "Tạo cửa hàng thành công!!!");
    }

    @Override
    public LaundryShop getLaundryShopByStoreOwner(StoreOwner storeOwner) {
        return laundryShopRepository.findByStoreOwner(storeOwner);
    }

    @Override
    public ApiResponse<LaundryShopDTO> updateLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        LaundryShop existingShop = laundryShopRepository.findByStoreOwner(storeOwner);
        if(existingShop == null) {
            return ApiResponse.error("Bạn chưa có cửa hàng để cập nhật!!!");
        }

        if (laundryShopDTO.getName() != null &&
                !laundryShopDTO.getName().equals(existingShop.getName()) &&
                laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return ApiResponse.error("Tên cửa hàng đã tồn tại, vui lòng chọn tên khác!");
        }

        if (laundryShopDTO.getName() != null) existingShop.setName(laundryShopDTO.getName());
        if (laundryShopDTO.getAddress() != null) existingShop.setAddress(laundryShopDTO.getAddress());
        if (laundryShopDTO.getOpeningHours() != null) existingShop.setOpeningHours(laundryShopDTO.getOpeningHours());
        if (laundryShopDTO.getDescription() != null) existingShop.setDescription(laundryShopDTO.getDescription());

        LaundryShop updatedShop = laundryShopRepository.save(existingShop);
        LaundryShopDTO responseDTO = convertToResponseDTO(updatedShop);

        return ApiResponse.success(responseDTO, "Cập nhật cửa hàng thành công!!!");
    }

    private Employee findEmployeeByInfo(EmployeeDTO employeeDTO) {
        Employee employee = null;

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty()) {
            employee = employeeRepository.findByUsername(employeeDTO.getUsername());
            if (employee != null) return employee;
        }

        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().isEmpty()) {
            employee = employeeRepository.findByEmail(employeeDTO.getEmail());
            if (employee != null) return employee;
        }

        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().isEmpty()) {
            employee = employeeRepository.findByPhone(employeeDTO.getPhone());
            return employee;
        }

        return null;
    }

    private LaundryShopDTO convertToResponseDTO(LaundryShop shop) {
        LaundryShopDTO dto = new LaundryShopDTO();
        dto.setId(shop.getId());
        dto.setName(shop.getName());
        dto.setAddress(shop.getAddress());
        dto.setOpeningHours(shop.getOpeningHours());
        dto.setDescription(shop.getDescription());
        dto.setAverageRating(shop.getAverageRating());

        LaundryShopDTO.StoreOwnerSimpleDTO ownerDTO = new LaundryShopDTO.StoreOwnerSimpleDTO();
        ownerDTO.setUsername(shop.getStoreOwner().getUsername());
        dto.setStoreOwner(ownerDTO);

        return dto;
    }
}