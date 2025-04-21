package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.*;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreOwnerService storeOwnerService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ApiResponse<StoreOwner> createStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        // Kiểm tra dữ liệu
        if (storeOwnerDTO.getPassword() == null || storeOwnerDTO.getPassword().isEmpty()) {
            return new ApiResponse<>("Password không được để trống", null);
        }

        //Kiểm tra password
        if(!UserValidator.isValidPassword(storeOwnerDTO.getPassword())){
            return new ApiResponse<>("Password không hợp lệ!!!", null);
        }

        //Kiểm tra email
        if(!UserValidator.isValidEmail(storeOwnerDTO.getEmail())){
            return new ApiResponse<>("Email không hợp lệ!!!", null);
        }
        if(storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail())){
            return new ApiResponse<>("Email đã tồn tại!!!", null);
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(storeOwnerDTO.getPhone())){
            return new ApiResponse<>("Phone phải có 10 chữ số!!!", null);
        }
        if(storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone())){
            return new ApiResponse<>("Phone đã tồn tại!!!", null);
        }

        // Mã hóa password trước khi lưu vào DB
        String encodedPassword = passwordEncoder.encode(storeOwnerDTO.getPassword());

        StoreOwner storeOwner = new StoreOwner(
                storeOwnerDTO.getUsername(),
                encodedPassword,
                storeOwnerDTO.getEmail(),
                storeOwnerDTO.getPhone(),
                storeOwnerDTO.getAddress(),
                Roles.StoreOwner
        );

        storeOwnerService.addStoreOwner(storeOwner);

        return new ApiResponse<>("Tạo tài khoản Store Owner thành công", storeOwner);
    }

    @Override
    public ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        if (storeOwnerDTO.getEmail() == null && storeOwnerDTO.getPhone() == null && storeOwnerDTO.getUsername() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        StoreOwner storeOwner = findStoreOwnerByInfo(storeOwnerDTO);

        if (storeOwner == null) {
            return new ApiResponse<>("Không tìm thấy Store Owner với thông tin đã cung cấp", null);
        }

        if (!storeOwnerRepository.existsById(storeOwner.getId())) {
            return new ApiResponse<>("Store Owner không tồn tại hoặc đã bị xóa trước đó", null);
        }

        storeOwnerService.deleteStoreOwner(storeOwner);

        return new ApiResponse<>("Đã xóa Store Owner thành công", null);
    }

    @Override
    public PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<StoreOwner> storeOwnerPage = storeOwnerRepository.findAll(pageable);

        List<StoreOwnerDTO> storeOwnerDTOs = storeOwnerPage.getContent().stream()
                .map(owner -> new StoreOwnerDTO(
                        owner.getUsername(),
                        null,
                        owner.getEmail(),
                        owner.getPhone(),
                        owner.getAddress(),
                        owner.getRoles(),
                        owner.getCreatedAt(),
                        owner.getUpdatedAt()
                ))
                .toList();

        Meta meta = new Meta(
                storeOwnerPage.getNumber() + 1,
                storeOwnerPage.getSize(),
                storeOwnerPage.getTotalElements(),
                storeOwnerPage.getTotalPages()
        );
        PagedData<StoreOwnerDTO> pagedData = new PagedData<>(meta, storeOwnerDTOs);
        PagedResponse<StoreOwnerDTO> response = new PagedResponse<>("Lấy danh sách thành công", pagedData);

        return ResponseEntity.ok(response).getBody();
    }

    @Override
    public PagedResponse<EmployeeDTO> getAllEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeDTO> employeeDTOS = employeePage.getContent().stream()
                .map(employee -> new EmployeeDTO(
                        employee.getUsername(),
                        null,
                        employee.getEmail(),
                        employee.getPhone(),
                        employee.getAddress(),
                        employee.getRoles(),
                        employee.getCreatedAt(),
                        employee.getUpdatedAt()
                ))
                .toList();

        Meta meta = new Meta(
                employeePage.getNumber() + 1,
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );

        PagedData<EmployeeDTO> pagedData = new PagedData<>(meta, employeeDTOS);
        PagedResponse<EmployeeDTO> response = new PagedResponse<>("Lấy danh sách thành công", pagedData);

        return ResponseEntity.ok(response).getBody();
    }

    private StoreOwner findStoreOwnerByInfo(StoreOwnerDTO storeOwnerDTO) {
        StoreOwner storeOwner = null;
        if (storeOwnerDTO.getEmail() != null) {
            storeOwner = storeOwnerRepository.findByEmail(storeOwnerDTO.getEmail());
        }
        if (storeOwner == null && storeOwnerDTO.getPhone() != null) {
            storeOwner = storeOwnerRepository.findByPhone(storeOwnerDTO.getPhone());
        }
        if (storeOwner == null && storeOwnerDTO.getUsername() != null) {
            storeOwner = storeOwnerRepository.findByUsername(storeOwnerDTO.getUsername());
        }
        return storeOwner;
    }

    @Override
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        ApiResponse<List<OrderResponse>> result;
        List<Order> allOrders = orderRepository.findAll();

        if (allOrders.isEmpty()) {
            result = new ApiResponse<>("Không có đơn hàng nào trong hệ thống", null);
        } else {
            List<OrderResponse> responseList = mapOrdersToOrderResponses(allOrders);
            result = new ApiResponse<>("Lấy danh sách tất cả đơn hàng thành công", responseList);
        }

        return result;
    }

    @Override
    public ApiResponse<List<OrderResponse>> getOrdersByCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));

        List<Order> customerOrders = orderRepository.findOrdersByCustomerId(customerId);

        if (customerOrders.isEmpty()) {
            return new ApiResponse<>("Khách hàng chưa có đơn hàng nào", null);
        }

        List<OrderResponse> responseList = mapOrdersToOrderResponses(customerOrders);
        return new ApiResponse<>("Lấy danh sách đơn hàng của khách hàng thành công", responseList);
    }

    private List<OrderResponse> mapOrdersToOrderResponses(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    OrderResponse orderResponse = new OrderResponse();
                    orderResponse.setId(order.getId());
                    orderResponse.setTotalAmount(order.getTotalAmount());
                    orderResponse.setOrderStatus(order.getOrderStatus());
                    orderResponse.setImgProduct(order.getImgProduct());
                    orderResponse.setLaundryShopName(order.getLaundryShop().getName());
                    orderResponse.setServiceCategoryName(order.getServiceCategory().getName());
                    orderResponse.setServiceName(order.getService().getName());
                    orderResponse.setServicePrice(order.getService().getPrice());
                    orderResponse.setOrderVolume(order.getOrderVolume());
                    orderResponse.setCreatedAt(order.getCreatedAt());
                    orderResponse.setInstructions(order.getInstructions());
                    return orderResponse;
                })
                .collect(Collectors.toList());
    }
}
