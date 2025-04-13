package com.example.laundry.services.impl;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.LaundryShopRepository; // Thêm repository cần thiết
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Service
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

    // Lấy store owner hiện tại
    private StoreOwner getCurrentStoreOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return storeOwnerRepository.findByUsername(currentUsername);
    }

    @Override
    public void addStoreOwner(StoreOwner storeOwner) {
        // Thực hiện logic thêm store owner
        storeOwnerRepository.save(storeOwner);
    }

    @Override
    public void deleteStoreOwner(StoreOwner storeOwner) {
        // Thực hiện logic xóa store owner
        storeOwnerRepository.delete(storeOwner);
    }

    @Override
    public ApiResponse<Employee> createEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        // Kiểm tra dữ liệu
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return new ApiResponse<>("Mật khẩu không được để trống", null);
        }

        // Kiểm tra password
        if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return new ApiResponse<>("Mật khẩu không hợp lệ!!!", null);
        }

        // Kiểm tra email
        if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return new ApiResponse<>("Email không hợp lệ!!!", null);
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return new ApiResponse<>("Email đã tồn tại!!!", null);
        }

        // Kiểm tra phone
        if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return new ApiResponse<>("Số điện thoại phải có 10 chữ số!!!", null);
        }

        if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return new ApiResponse<>("Số điện thoại đã tồn tại!!!", null);
        }

        //Kiểm tra xem StoreOwner có shop chưa
        LaundryShop laundryShop = laundryShopRepository.findByStoreOwner(storeOwner);
        if (laundryShop == null) {
            return new ApiResponse<>("StoreOwner chưa có shop, không thể tạo nhân viên", null);
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

        //Gán shop vào employee
        employee.setShop(laundryShop);

        // Sử dụng service để thêm employee với quan hệ storeOwner
        employeeService.addEmployee(employee);

        return new ApiResponse<>("Tạo tài khoản nhân viên thành công", employee);
    }

    @Override
    public ApiResponse<String> deleteEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        // Kiểm tra xem tồn tại hay không
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        // Tìm Employee theo các thông tin cung cấp
        Employee employee = findEmployeeByInfo(employeeDTO);

        if (employee == null) {
            return new ApiResponse<>("Không tìm thấy nhân viên với thông tin đã cung cấp!!!", null);
        }

        // Kiểm tra Employee có thuộc về StoreOwner hiện tại không
        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return new ApiResponse<>("Bạn không có quyền xóa nhân viên này", null);
        }

        employeeService.deleteEmployee(employee);

        return new ApiResponse<>("Đã xóa nhân viên thành công", null);
    }

    @Override
    public ApiResponse<Employee> updateEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return new ApiResponse<>("Cần cung cấp ít nhất một thông tin để tìm Employee.", null);
        }

        Employee employee = findEmployeeByInfo(employeeDTO);
        if (employee == null) {
            return new ApiResponse<>("Không tìm thấy Employee với thông tin đã cung cấp.", null);
        }

        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return new ApiResponse<>("Bạn không có quyền cập nhật Employee này", null);
        }

        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().equals(employee.getEmail())) {
            if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
                return new ApiResponse<>("Email không hợp lệ!!!", null);
            }
            if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                return new ApiResponse<>("Email đã tồn tại!!!", null);
            }
            employee.setEmail(employeeDTO.getEmail());
        }

        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().equals(employee.getPhone())) {
            if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
                return new ApiResponse<>("Phone phải có 10 chữ số!!!", null);
            }
            if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
                return new ApiResponse<>("Phone đã tồn tại!!!", null);
            }
            employee.setPhone(employeeDTO.getPhone());
        }

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty() &&
                !employeeDTO.getUsername().equals(employee.getUsername())) {
            employee.setUsername(employeeDTO.getUsername());
        }

        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
                return new ApiResponse<>("Password không hợp lệ!!!", null);
            }
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }

        employeeService.updateEmployee(employee);

        return new ApiResponse<>("Cập nhật Employee thành công!", employee);
    }

    @Override
    public ApiResponse<LaundryShopDTO> createShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        //Kiểm tra xem trước đó đã quản lý shop nào chưa
        if(laundryShopRepository.existsByStoreOwner(storeOwner)) {
            return new ApiResponse<>("Bạn đã có cửa hàng rồi, không được tạo thêm");
        }

        // kiểm tra dữ liệu
        if(laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return new ApiResponse<>("Tên cửa hàng đã tồn tại vui lòng chọn tên khác!!!");
        }

        if(laundryShopDTO.getName() == null || laundryShopDTO.getName().isEmpty()) {
            return new ApiResponse<>("Tên cửa hàng không được để trống");
        }

        if(laundryShopDTO.getAddress() == null || laundryShopDTO.getAddress().isEmpty()) {
            return new ApiResponse<>("Địa chỉ cửa hàng không được để trống");
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

        // Lưu shop vào database
        LaundryShop createdShop = laundryShopRepository.save(laundryShop);

        LaundryShopDTO responseDTO = convertToResponseDTO(createdShop);

        return new ApiResponse<>("Tạo cửa hàng thành công!!!", responseDTO);
    }

    @Override
    public LaundryShop getLaundryShopByStoreOwner(StoreOwner storeOwner) {
        return laundryShopRepository.findByStoreOwner(storeOwner);
    }

    @Override
    public ApiResponse<LaundryShopDTO> updateLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        LaundryShop existingShop =  laundryShopRepository.findByStoreOwner(storeOwner);
        if(existingShop == null) {
            return new ApiResponse<>("Bạn chưa có cửa hàng để cập nhật!!!", null);
        }

        if (laundryShopDTO.getName() != null &&
                !laundryShopDTO.getName().equals(existingShop.getName()) &&
                laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return new ApiResponse<>("Tên cửa hàng đã tồn tại, vui lòng chọn tên khác!", null);
        }

        if (laundryShopDTO.getName() != null) existingShop.setName(laundryShopDTO.getName());
        if (laundryShopDTO.getAddress() != null) existingShop.setAddress(laundryShopDTO.getAddress());
        if (laundryShopDTO.getOpeningHours() != null) existingShop.setOpeningHours(laundryShopDTO.getOpeningHours());
        if (laundryShopDTO.getDescription() != null) existingShop.setDescription(laundryShopDTO.getDescription());

        LaundryShop updatedShop = laundryShopRepository.save(existingShop);
        LaundryShopDTO responseDTO = convertToResponseDTO(updatedShop);

        return new ApiResponse<>("Cập nhật cửa hàng thành công!!!", responseDTO);
    }

    // Thêm phương thức tìm employee theo thông tin
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