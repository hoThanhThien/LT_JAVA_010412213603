package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Roles;
import com.example.laundry.repository.*;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.OrderService;
import com.example.laundry.services.RefreshTokenService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.PaginationUtils;
import com.example.laundry.utils.UserValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Customer addCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private FirebaseAuth firebaseAuth;

    @Autowired
    private LaundryShopRepository laundryShopRepository;


    @Override
    @Transactional
    public CustomerResponseDTO register(RegisterRequest registerRequest) {
        if (firebaseAuth == null) {
            return new CustomerResponseDTO("Chưa cấu hình Firebase Admin (thiếu firebase-service-account.json)", null);
        }
        if (registerRequest == null || registerRequest.getIdToken() == null || registerRequest.getIdToken().isBlank()) {
            return new CustomerResponseDTO("Thiếu token xác thực Firebase", null);
        }

        // Xác minh idToken từ Firebase trước khi thực hiện bất kỳ thao tác ghi DB nào
        FirebaseToken decodedToken;
        try {
            decodedToken = firebaseAuth.verifyIdToken(registerRequest.getIdToken());
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            log.warn("Firebase authentication error during registration: {}", e.getMessage());
            return new CustomerResponseDTO("Xác minh Firebase thất bại", null);
        }

        String verifiedPhone = null;
        Map<String, Object> claims = decodedToken.getClaims();
        if (claims != null && claims.containsKey("phone_number")) {
            verifiedPhone = (String) claims.get("phone_number");
            if (verifiedPhone != null && verifiedPhone.startsWith("+84")) {
                verifiedPhone = "0" + verifiedPhone.substring(3);
            }
        }

        if (verifiedPhone == null || verifiedPhone.isEmpty()) {
            return new CustomerResponseDTO("Số điện thoại không tồn tại", null);
        }

        if (!UserValidator.isValidPhone(verifiedPhone)) {
            return new CustomerResponseDTO("Số điện thoại không hợp lệ! Phải có đúng 10 chữ số.", null);
        }

        if (!UserValidator.isValidPassword(registerRequest.getPassword())) {
            return new CustomerResponseDTO("Mật khẩu không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.", null);
        }

        // Kiểm tra tồn tại trên toàn hệ thống (mọi bảng role của User)
        if (userRepository.existsByPhone(verifiedPhone) || customerRepository.findByPhone(verifiedPhone).isPresent()) {
            return new CustomerResponseDTO("Số điện thoại đã được đăng ký trong hệ thống", null);
        }

        // Không nuốt RuntimeException sau khi ghi DB để Spring tự rollback transaction nếu bước sau lỗi
        Customer customer = new Customer();
        customer.setPhone(verifiedPhone);
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setUsername(registerRequest.getUsername());
        customer.setRoles(Roles.Customer);

        Customer savedCustomer = customerRepository.save(customer);
        String accessToken = jwtUtil.generateAccessToken(savedCustomer.getId(), savedCustomer.getUsername(), savedCustomer.getRoles());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedCustomer);
        String refreshTokenString = refreshToken.getToken();

        CustomerResponseDTO.AccountInfo accountInfor = new CustomerResponseDTO.AccountInfo(
                savedCustomer.getId(),
                savedCustomer.getUsername(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getAddress(),
                Roles.Customer
        );

        CustomerResponseDTO.DataInfo dataInfo = new CustomerResponseDTO.DataInfo(
                accessToken,
                refreshTokenString,
                accountInfor
        );

        return new CustomerResponseDTO("Đăng ký thành công!!!", dataInfo);
    }

    @Override
    @Transactional
    public ApiResponse<OrderResponse> bookService(Customer customer, OrderDTO orderDTO) {
        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Customer not found");
        }
        if (orderDTO == null) {
            return new ApiResponse<>("Thiếu thông tin đơn hàng!");
        }
        // Kiem tra customer
        Customer customerId = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer.getId()));
        // Load LaundryShop từ database
        LaundryShop laundryShop = null;
        if (orderDTO.getLaundryShop() != null && orderDTO.getLaundryShop().getId() != null) {
            long shopIdLong = orderDTO.getLaundryShop().getId();
            if (shopIdLong <= 0 || shopIdLong > Integer.MAX_VALUE) {
                return new ApiResponse<>("ID cửa hàng không hợp lệ!");
            }
            laundryShop = laundryShopRepository.findById((int) shopIdLong)
                    .orElseThrow(() -> new RuntimeException("LaundryShop not found with id: " + shopIdLong));
        } else {
            return new ApiResponse<>("Vui lòng chọn cửa hàng giặt là!!!");
        }

        // Load ServiceCategory từ database
        ServiceCategory serviceCategory = null;
        if (orderDTO.getServiceCategory() != null && orderDTO.getServiceCategory().getId() != null) {
            serviceCategory = serviceCategoryRepository.findById(orderDTO.getServiceCategory().getId())
                    .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id: " + orderDTO.getServiceCategory().getId()));
        } else {
            return new ApiResponse<>("Vui lòng chọn loại dịch vụ!!!");
        }

        // Load Service từ database và lấy giá
        Service service = null;
        if (orderDTO.getService() != null && orderDTO.getService().getId() != null) {
            service = serviceRepository.findById(orderDTO.getService().getId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + orderDTO.getService().getId()));
        } else {
            return new ApiResponse<>("Vui lòng chọn dịch vụ!!!");
        }

        // Kiem tra du lieu
        if (serviceCategory.getShop() == null || !serviceCategory.getShop().getId().equals(laundryShop.getId())) {
            return new ApiResponse<>("Loại dịch vụ không thuộc cửa hàng đã chọn!");
        }

        if (service.getCategory() == null || !service.getCategory().getId().equals(serviceCategory.getId())) {
            return new ApiResponse<>("Dịch vụ không thuộc loại dịch vụ đã chọn!");
        }

        Double orderVolume = orderDTO.getOrderVolume();
        if (orderVolume == null || !Double.isFinite(orderVolume) || orderVolume <= 0) {
            return new ApiResponse<>("Khối lượng đồ không được để trống và phải lớn hơn 0");
        }

        Double servicePrice = service.getPrice();
        if (servicePrice == null || !Double.isFinite(servicePrice) || servicePrice <= 0) {
            return new ApiResponse<>("Giá dịch vụ không hợp lệ!");
        }

        String resolvedAddress = (orderDTO.getAddress() != null && !orderDTO.getAddress().isBlank())
                ? orderDTO.getAddress().trim()
                : customerId.getAddress();
        if (resolvedAddress == null || resolvedAddress.isBlank()) {
            return new ApiResponse<>("Vui lòng nhập địa chỉ giao nhận đơn hàng!");
        }

        boolean customerChanged = false;
        if (orderDTO.getPhone() != null && !orderDTO.getPhone().isBlank()) {
            String trimmedPhone = orderDTO.getPhone().trim();
            if (!UserValidator.isValidPhone(trimmedPhone)) {
                return new ApiResponse<>("Số điện thoại liên hệ không hợp lệ! Phải có đúng 10 chữ số.");
            }
            if ((customerId.getPhone() == null || customerId.getPhone().isBlank())
                    && !userRepository.existsByPhone(trimmedPhone)) {
                customerId.setPhone(trimmedPhone);
                customerChanged = true;
            }
        }
        if (customerId.getAddress() == null || customerId.getAddress().isBlank()) {
            customerId.setAddress(resolvedAddress);
            customerChanged = true;
        }
        if (customerChanged) {
            customerRepository.save(customerId);
        }

        Order order = new Order();
        order.setCustomer(customerId);
        order.setUsername(customerId.getUsername());
        order.setLaundryShop(laundryShop);
        order.setServiceCategory(serviceCategory);
        order.setService(service);
        order.setOrderVolume(orderVolume);
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setPaymentStatus(com.example.laundry.models.order.PaymentStatus.PENDING);
        order.setInstructions(orderDTO.getInstructions());
        order.setImgProduct(orderDTO.getImgProduct());
        order.setAddress(resolvedAddress);
        double totalAmount = BigDecimal.valueOf(orderVolume)
                .multiply(BigDecimal.valueOf(servicePrice))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderService.save(order);
        OrderResponse responseDTO = OrderResponse.from(savedOrder);

        return new ApiResponse<>("Bạn đã tạo đơn giặt hàng thành công. Vui lòng chú ý thông báo của chúng tôi!", responseDTO);
    }

    @Override
    public PagedResponse<OrderResponse> historyOrder(Customer customer, int page, int size) {
        Pageable pageable = PaginationUtils.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByCustomer_Id(customer.getId(), pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        Meta<OrderResponse> meta = new Meta<>(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );

        return new PagedResponse<>("Lấy lịch sử đơn hàng thành công", new PagedData<>(meta, orderResponses));
    }

    @Override
    @Transactional
    public ApiResponse<CustomerDTO> updateCustomer(Customer customer, CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return new ApiResponse<>("Thiếu thông tin cần cập nhật", null);
        }

        // Validate email if provided
        if (customerDTO.getEmail() != null && !customerDTO.getEmail().isEmpty()) {
            if (!UserValidator.isValidEmail(customerDTO.getEmail())) {
                return new ApiResponse<>("Email không hợp lệ!", null);
            }

            if (!customerDTO.getEmail().equals(customer.getEmail()) &&
                    (userRepository.existsByEmail(customerDTO.getEmail()) ||
                            customerRepository.existsByEmail(customerDTO.getEmail()))) {
                return new ApiResponse<>("Email đã tồn tại trong hệ thống", null);
            }

            customer.setEmail(customerDTO.getEmail());
        }

        // Validate phone if provided
        if (customerDTO.getPhone() != null && !customerDTO.getPhone().isEmpty()) {
            if (!UserValidator.isValidPhone(customerDTO.getPhone())) {
                return new ApiResponse<>("Số điện thoại không hợp lệ! Phải có đúng 10 chữ số.", null);
            }

            if (!customerDTO.getPhone().equals(customer.getPhone()) &&
                    (userRepository.existsByPhone(customerDTO.getPhone()) ||
                            customerRepository.existsByPhone(customerDTO.getPhone()))) {
                return new ApiResponse<>("Số điện thoại đã tồn tại trong hệ thống", null);
            }

            customer.setPhone(customerDTO.getPhone());
        }

        // Update other fields
        if (customerDTO.getUsername() != null && !customerDTO.getUsername().isEmpty()) {
            customer.setUsername(customerDTO.getUsername());
        }

        if (customerDTO.getAddress() != null) {
            customer.setAddress(customerDTO.getAddress());
        }

        boolean passwordChanged = false;
        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            if (!UserValidator.isValidPassword(customerDTO.getPassword())) {
                return new ApiResponse<>("Mật khẩu không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.", null);
            }
            customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            passwordChanged = true;
        }

        if (customerDTO.getAvtUser() != null && !customerDTO.getAvtUser().isEmpty()) {
            customer.setAvtUser(customerDTO.getAvtUser());
        }

        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(customer);
        if (passwordChanged) {
            refreshTokenService.deleteByUser(updatedCustomer);
        }

        // Build response (không trả hash mật khẩu ra ngoài)
        CustomerDTO responseDTO = new CustomerDTO();
        responseDTO.setId(updatedCustomer.getId());
        responseDTO.setUsername(updatedCustomer.getUsername());
        responseDTO.setEmail(updatedCustomer.getEmail());
        responseDTO.setPhone(updatedCustomer.getPhone());
        responseDTO.setAddress(updatedCustomer.getAddress());
        responseDTO.setRole(updatedCustomer.getRoles());
        responseDTO.setAvtUser(updatedCustomer.getAvtUser());
        responseDTO.setCreatedAt(updatedCustomer.getCreatedAt());
        responseDTO.setUpdatedAt(updatedCustomer.getUpdatedAt());

        return new ApiResponse<>("Cập nhật thông tin thành công", responseDTO);
    }
}

