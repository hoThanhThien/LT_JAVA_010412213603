package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.*;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.OrderService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.PaginationUtils;
import com.example.laundry.utils.UserValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreOwnerService storeOwnerService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LaundryShopRepository laundryShopRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private EmployeeService employeeService;

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
        if(userRepository.existsByEmail(storeOwnerDTO.getEmail()) || storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail())){
            return new ApiResponse<>("Email đã tồn tại!!!", null);
        }

        //Kiểm tra phone
        if(!UserValidator.isValidPhone(storeOwnerDTO.getPhone())){
            return new ApiResponse<>("Phone phải có 10 chữ số!!!", null);
        }
        if(userRepository.existsByPhone(storeOwnerDTO.getPhone()) || storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone())){
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
    public ApiResponse<StoreOwnerDTO> updateStoreOwner(StoreOwnerDTO storeOwnerDTO) {
      StoreOwner storeOwner = findStoreOwnerByInfo(storeOwnerDTO);

      if (storeOwner == null) {
        return new ApiResponse<>("Không tìm thấy chủ cửa hàng với thông tin đã cung cấp!", null);
      }

      if (storeOwnerDTO.getUsername() != null && !storeOwnerDTO.getUsername().isEmpty()) {
        storeOwner.setUsername(storeOwnerDTO.getUsername());
      }

      if (storeOwnerDTO.getEmail() != null && !storeOwnerDTO.getEmail().isEmpty()) {
        if (!UserValidator.isValidEmail(storeOwnerDTO.getEmail())) {
          return new ApiResponse<>("Email không hợp lệ!", null);
        }
        if (!storeOwnerDTO.getEmail().equals(storeOwner.getEmail()) &&
                (userRepository.existsByEmail(storeOwnerDTO.getEmail()) ||
                        storeOwnerRepository.existsByEmail(storeOwnerDTO.getEmail()))) {
          return new ApiResponse<>("Email đã tồn tại!", null);
        }
        storeOwner.setEmail(storeOwnerDTO.getEmail());
      }

      if (storeOwnerDTO.getPhone() != null && !storeOwnerDTO.getPhone().isEmpty()) {
        if (!UserValidator.isValidPhone(storeOwnerDTO.getPhone())) {
          return new ApiResponse<>("Phone phải có 10 chữ số!", null);
        }
        if (!storeOwnerDTO.getPhone().equals(storeOwner.getPhone()) &&
                (userRepository.existsByPhone(storeOwnerDTO.getPhone()) ||
                        storeOwnerRepository.existsByPhone(storeOwnerDTO.getPhone()))) {
          return new ApiResponse<>("Phone đã tồn tại!", null);
        }
        storeOwner.setPhone(storeOwnerDTO.getPhone());
      }

      if (storeOwnerDTO.getAddress() != null) {
        storeOwner.setAddress(storeOwnerDTO.getAddress());
      }

      if (storeOwnerDTO.getPassword() != null && !storeOwnerDTO.getPassword().isEmpty()) {
        if (!UserValidator.isValidPassword(storeOwnerDTO.getPassword())) {
          return new ApiResponse<>("Mật khẩu không hợp lệ!", null);
        }

        storeOwner.setPassword(passwordEncoder.encode(storeOwnerDTO.getPassword()));
      }

      if (storeOwnerDTO.getAvtUser() != null && !storeOwnerDTO.getAvtUser().isEmpty()) {
        storeOwner.setAvtUser(storeOwnerDTO.getAvtUser());
      }

      StoreOwner updatedStoreOwner = storeOwnerRepository.save(storeOwner);

      StoreOwnerDTO responseDTO = new StoreOwnerDTO();
      responseDTO.setId(updatedStoreOwner.getId());
      responseDTO.setUsername(updatedStoreOwner.getUsername());
      responseDTO.setEmail(updatedStoreOwner.getEmail());
      responseDTO.setPhone(updatedStoreOwner.getPhone());
      responseDTO.setAddress(updatedStoreOwner.getAddress());
      responseDTO.setRole(updatedStoreOwner.getRoles());
      responseDTO.setAvtUser(updatedStoreOwner.getAvtUser());
      responseDTO.setCreatedAt(updatedStoreOwner.getCreatedAt());
      responseDTO.setUpdatedAt(updatedStoreOwner.getUpdatedAt());

      return new ApiResponse<>("Cập nhật chủ cửa hàng thành công!", responseDTO);
    }

    @Override
    @Transactional
    public ApiResponse<String> removeStoreOwner(StoreOwnerDTO storeOwnerDTO) {
        if (storeOwnerDTO.getId() == null && storeOwnerDTO.getEmail() == null && storeOwnerDTO.getPhone() == null && storeOwnerDTO.getUsername() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        StoreOwner storeOwner = findStoreOwnerByInfo(storeOwnerDTO);

        if (storeOwner == null) {
            return new ApiResponse<>("Không tìm thấy Store Owner với thông tin đã cung cấp", null);
        }

        if (!storeOwnerRepository.existsById(storeOwner.getId())) {
            return new ApiResponse<>("Store Owner không tồn tại hoặc đã bị xóa trước đó", null);
        }

        LaundryShop shop = laundryShopRepository.findByStoreOwner(storeOwner);
        if (shop != null) {
            if (orderRepository.existsByLaundryShop(shop)) {
                return new ApiResponse<>("Không thể xóa chủ cửa hàng vì cửa hàng vẫn còn đơn hàng!", null);
            }
            List<Employee> employees = employeeRepository.findByStoreOwner(storeOwner);
            for (Employee emp : employees) {
                emp.setShop(null);
            }
            employeeRepository.saveAll(employees);
            laundryShopRepository.delete(shop);
            laundryShopRepository.flush();
        }

        storeOwnerService.deleteStoreOwner(storeOwner);

        return new ApiResponse<>("Đã xóa Store Owner thành công", "OK");
    }

    @Override
    public PagedResponse<StoreOwnerDTO> getAllStoreOwners(int page, int size) {
        Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
        Page<StoreOwner> storeOwnerPage = storeOwnerRepository.findAll(pageable);

        List<StoreOwnerDTO> storeOwnerDTOs = storeOwnerPage.getContent().stream()
                .map(owner -> new StoreOwnerDTO(
                        owner.getUsername(),
                        null,
                        owner.getEmail(),
                        owner.getPhone(),
                        owner.getAddress(),
                        owner.getRoles(),
                        owner.getAvtUser(),
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
    public PagedResponse<StoreOwnerWithEmployeeDTO> getAllEmployeesBelongToStoreOwner(int page, int size) {
      Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
      Page<StoreOwner> storeOwnerPage = storeOwnerRepository.findAll(pageable);

      List<StoreOwner> owners = storeOwnerPage.getContent();
      List<UUID> ownerIds = owners.stream().map(StoreOwner::getId).toList();

      Map<UUID, LaundryShop> shopsByOwnerId = ownerIds.isEmpty()
              ? Collections.emptyMap()
              : laundryShopRepository.findByStoreOwner_IdIn(ownerIds).stream()
                      .filter(shop -> shop.getStoreOwner() != null)
                      .collect(Collectors.toMap(
                              shop -> shop.getStoreOwner().getId(),
                              shop -> shop,
                              (first, second) -> first
                      ));

      Map<UUID, List<Employee>> employeesByOwnerId = ownerIds.isEmpty()
              ? Collections.emptyMap()
              : employeeRepository.findByStoreOwner_IdIn(ownerIds).stream()
                      .filter(emp -> emp.getStoreOwner() != null)
                      .collect(Collectors.groupingBy(emp -> emp.getStoreOwner().getId()));

      List<StoreOwnerWithEmployeeDTO> ownerDTOS = owners.stream()
              .map(owner -> {
                LaundryShop shop = shopsByOwnerId.get(owner.getId());
                Long shopId = shop != null ? shop.getId() : null;
                String shopName = shop != null ? shop.getName() : null;

                List<EmployeeDTO> employees = employeesByOwnerId.getOrDefault(owner.getId(), Collections.emptyList()).stream()
                        .map(employee -> new EmployeeDTO(
                                employee.getUsername(),
                                employee.getPhone(),
                                employee.getEmail(),
                                employee.getAddress(),
                                employee.getAvtUser(),
                                employee.getRoles(),
                                employee.getCreatedAt(),
                                employee.getUpdatedAt()
                        )).toList();

                return new StoreOwnerWithEmployeeDTO(
                        owner.getId(),
                        owner.getUsername(),
                        owner.getPhone(),
                        owner.getEmail(),
                        owner.getAddress(),
                        owner.getAvtUser(),
                        shopId,
                        shopName,
                        owner.getRoles(),
                        owner.getCreatedAt(),
                        owner.getUpdatedAt(),
                        employees
                );
              }).toList();

      Meta<StoreOwnerWithEmployeeDTO> meta = new Meta<>(
              storeOwnerPage.getNumber() + 1,
              storeOwnerPage.getSize(),
              storeOwnerPage.getTotalElements(),
              storeOwnerPage.getTotalPages()
      );

      PagedData<StoreOwnerWithEmployeeDTO> pagedData = new PagedData<>(meta, ownerDTOS);

      return new PagedResponse<>("Lấy danh sách nhân viên thành công!!!", pagedData);
    }

    @Override
    public PagedResponse<OrderResponse> getAllOrders(int page, int size) {
          Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
          Page<Order> orderPage = orderRepository.findAll(pageable);

          List<OrderResponse> responseList = mapOrdersToOrderResponses(orderPage.getContent());

          Meta meta = new Meta(
                  orderPage.getNumber() + 1,
                  orderPage.getSize(),
                  orderPage.getTotalElements(),
                  orderPage.getTotalPages()
          );

          PagedData<OrderResponse> pagedData = new PagedData<>(meta, responseList);
          return new PagedResponse<>("Lấy danh sách tất cả đơn hàng thành công", pagedData);
      }

    private StoreOwner findStoreOwnerByInfo(StoreOwnerDTO storeOwnerDTO) {
    StoreOwner storeOwner = null;
    if (storeOwnerDTO.getId() != null) {
      storeOwner = storeOwnerRepository.findById(storeOwnerDTO.getId()).orElse(null);
    }
    if (storeOwner == null && storeOwnerDTO.getPhone() != null && !storeOwnerDTO.getPhone().isEmpty()) {
      storeOwner = storeOwnerRepository.findByPhone(storeOwnerDTO.getPhone());
    }
    if (storeOwner == null && storeOwnerDTO.getEmail() != null && !storeOwnerDTO.getEmail().isEmpty()) {
      storeOwner = storeOwnerRepository.findByEmail(storeOwnerDTO.getEmail());
    }
    if (storeOwner == null && storeOwnerDTO.getUsername() != null && !storeOwnerDTO.getUsername().isEmpty()) {
      storeOwner = storeOwnerRepository.findByUsername(storeOwnerDTO.getUsername());
    }
    return storeOwner;
  }

  @Override
  public PagedResponse<OrderResponse> getOrdersByCustomer(UUID customerId, int page, int size) {
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));

    Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
    Page<Order> customerOrders = orderRepository.findByCustomer_Id(customer.getId(), pageable);

    if (customerOrders.getContent().isEmpty()) {
      return new PagedResponse<>("Khách hàng chưa có đơn hàng nào", null);
    }

    List<OrderResponse> responseList = mapOrdersToOrderResponses(customerOrders.getContent());

    Meta meta = new Meta(
            customerOrders.getNumber() + 1,
            customerOrders.getSize(),
            customerOrders.getTotalElements(),
            customerOrders.getTotalPages()
    );

    PagedData<OrderResponse> pagedData = new PagedData<>(meta, responseList);
    return new PagedResponse<>("Lấy danh sách đơn hàng của khách hàng thành công", pagedData);
  }

    @Override
    public PagedResponse<CustomerDTO> getAllCustomers(int page, int size) {
        Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerDTO> customerDTOs = customerPage.getContent().stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getUsername(),
                        null,
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getAddress(),
                        customer.getRoles(),
                        customer.getAvtUser(),
                        customer.getCreatedAt(),
                        customer.getUpdatedAt()
                ))
                .toList();

        Meta meta = new Meta(
                customerPage.getNumber() + 1,
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages()
        );

        PagedData<CustomerDTO> pagedData = new PagedData<>(meta, customerDTOs);
        PagedResponse<CustomerDTO> response = new PagedResponse<>("Lấy danh sách thành công", pagedData);

        return ResponseEntity.ok(response).getBody();
    }

    @Override
    public PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size) {
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return new PagedResponse<>("Trạng thái đơn hàng không hợp lệ", null);
        }

        Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByOrderStatus(orderStatus, pageable);


        List<OrderResponse> responseList = mapOrdersToOrderResponses(orderPage.getContent());

        Meta meta = new Meta(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );

        PagedData<OrderResponse> pagedData = new PagedData<>(meta, responseList);
        return new PagedResponse<>("Lấy danh sách đơn hàng theo trạng thái thành công", pagedData);
    }

    private List<OrderResponse> mapOrdersToOrderResponses(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
