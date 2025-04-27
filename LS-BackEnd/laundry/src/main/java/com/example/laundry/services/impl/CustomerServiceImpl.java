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
import com.example.laundry.utils.UserValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private LaundryShopRepository laundryShopRepository;


    @Override
    @Transactional
    public CustomerResponseDTO register(RegisterRequest registerRequest) {
        // Xác minh idToken từ firebase để lấy số điện thoại
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(registerRequest.getIdToken());
            String verifiedPhone = null;

            Map<String, Object> claims = decodedToken.getClaims();

            if (claims.containsKey("phone_number")) {
                verifiedPhone = (String) claims.get("phone_number");

                //Chuyển +84 thành 0
                if (verifiedPhone.startsWith("+84")) {
                    verifiedPhone = "0" + verifiedPhone.substring(3);
                }
            }

            //Kiểm tra xem có dữ liệu chưa
            if (verifiedPhone == null || verifiedPhone.isEmpty()) {
                return new CustomerResponseDTO("Số điện thoại không tồn tại", null);
            }

            //Kiểm tra tồn tại
            if (customerRepository.findByPhone(verifiedPhone).isPresent()) {
                System.out.println("Số điện thoại đã tồn tại trong hệ thống: " + verifiedPhone);
                return new CustomerResponseDTO("Số điện thoại đã được đăng ký trong hệ thống", null);
            }

            //Tạo người dùng
            Customer customer = new Customer();
            if (!UserValidator.isValidPhone(verifiedPhone)) {
                return new CustomerResponseDTO("Số điện thoại không hợp lệ! Phải có đúng 10 chữ số.", null);
            }
            customer.setPhone(verifiedPhone);
            if (!UserValidator.isValidPassword(registerRequest.getPassword())) {
                return new CustomerResponseDTO("Mật khẩu không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.", null);
            }
            customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            customer.setUsername(registerRequest.getUsername());
            customer.setRoles(Roles.Customer);

            Customer savedCustomer = customerRepository.save(customer);
            //Tạo access token
            String accessToken = jwtUtil.generateAccessToken(savedCustomer.getUsername(), savedCustomer.getRoles());

            //Tạo refresh token
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
        } catch (FirebaseAuthException e) {
            System.err.println("Firebase authentication error: " + e.getMessage());
            return new CustomerResponseDTO("Xác minh Firebase thất bại: " + e.getMessage(), null);
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return new CustomerResponseDTO("Đăng ký thất bại: " + e.getMessage(), null);
        }

    }

    @Override
    public ApiResponse<OrderResponse> bookService(Customer customer, OrderDTO orderDTO) {
        // Kiem tra customer
        Customer customerId = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Load LaundryShop từ database
        LaundryShop laundryShop = null;
        if (orderDTO.getLaundryShop() != null && orderDTO.getLaundryShop().getId() != null) {
            laundryShop = laundryShopRepository.findById(Math.toIntExact(orderDTO.getLaundryShop().getId()))
                    .orElseThrow(() -> new RuntimeException("LaundryShop not found with id: " + orderDTO.getLaundryShop().getId()));
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
        Double orderVolume = orderDTO.getOrderVolume();
        if (orderVolume == null || orderDTO.getOrderVolume() <= 0) {
            return new ApiResponse<>("Khối lượng đồ không được để trống và phải lớn hơn 0");
        }

        Order order = new Order();
        order.setCustomer(customerId);
        order.setUsername(customer.getUsername());
        order.setLaundryShop(laundryShop);
        order.setServiceCategory(serviceCategory);
        order.setService(service);
        order.setOrderVolume(orderDTO.getOrderVolume());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setInstructions(orderDTO.getInstructions());
        order.setImgProduct(orderDTO.getImgProduct());
        order.setAddress(orderDTO.getAddress());
        double totalAmount = orderDTO.getOrderVolume() * service.getPrice();
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderService.save(order);

        OrderResponse responseDTO = new OrderResponse();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setUsername(customer.getUsername());
        responseDTO.setPhone(customer.getPhone());
        responseDTO.setEmail(customer.getEmail());
        responseDTO.setAddress(savedOrder.getAddress());
        responseDTO.setTotalAmount(savedOrder.getTotalAmount());
        responseDTO.setOrderStatus(savedOrder.getOrderStatus());
        responseDTO.setImgProduct(savedOrder.getImgProduct());
        responseDTO.setLaundryShopName(laundryShop.getName());
        responseDTO.setServiceCategoryName(serviceCategory.getName());
        responseDTO.setServiceName(service.getName());
        responseDTO.setServicePrice(service.getPrice());
        responseDTO.setOrderVolume(savedOrder.getOrderVolume());
        responseDTO.setCreatedAt(savedOrder.getCreatedAt());
        responseDTO.setInstructions(savedOrder.getInstructions());
        responseDTO.setStatus(savedOrder.getOrderStatus());


        return new ApiResponse<>("Bạn đã tạo đơn giặt hàng thành công. Vui lòng chú ý thông báo của chúng tôi!", responseDTO);
    }

    @Override
    public PagedResponse<OrderResponse> historyOrder(Customer customer, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByCustomer_Id(customer.getId(), pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(order -> {
                    OrderResponse orderResponse = new OrderResponse();
                    orderResponse.setId(order.getId());
                    orderResponse.setTotalAmount(order.getTotalAmount());
                    orderResponse.setOrderStatus(order.getOrderStatus());
                    orderResponse.setLaundryShopName(order.getLaundryShop().getName());
                    orderResponse.setServiceCategoryName(order.getServiceCategory().getName());
                    orderResponse.setServiceName(order.getService().getName());
                    orderResponse.setServicePrice(order.getService().getPrice());
                    orderResponse.setOrderVolume(order.getOrderVolume());
                    orderResponse.setCreatedAt(order.getCreatedAt());
                    orderResponse.setInstructions(order.getInstructions());
                    orderResponse.setImgProduct(order.getImgProduct());
                    orderResponse.setStatus(order.getOrderStatus());
                    orderResponse.setPaymentStatus(order.getPaymentStatus());
                    return orderResponse;

                })
                .collect(Collectors.toList());

        Meta meta = new Meta(
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
        try {
            // Validate email if provided
            if (customerDTO.getEmail() != null && !customerDTO.getEmail().isEmpty()) {
                if (!UserValidator.isValidEmail(customerDTO.getEmail())) {
                    return new ApiResponse<>("Email không hợp lệ!", null);
                }

                if (customerRepository.existsByEmail(customerDTO.getEmail()) &&
                        !customerDTO.getEmail().equals(customer.getEmail())) {
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
                        customerRepository.existsByPhone(customerDTO.getPhone())) {
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

            if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
                if (!UserValidator.isValidPassword(customerDTO.getPassword())) {
                    return new ApiResponse<>("Mật khẩu không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.", null);
                }
                customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            }

            if (customerDTO.getAvtUser() != null && customerDTO.getAvtUser().isEmpty()) {
                customer.setAvtUser(customerDTO.getAvtUser());
            }

            // Save the updated customer
            Customer updatedCustomer = customerRepository.save(customer);

            // Build response
            CustomerDTO responseDTO = new CustomerDTO();
            responseDTO.setUsername(updatedCustomer.getUsername());
            responseDTO.setPassword(updatedCustomer.getPassword());
            responseDTO.setEmail(updatedCustomer.getEmail());
            responseDTO.setPhone(updatedCustomer.getPhone());
            responseDTO.setAddress(updatedCustomer.getAddress());
            responseDTO.setRole(updatedCustomer.getRoles());
            responseDTO.setAvtUser(updatedCustomer.getAvtUser());
            responseDTO.setCreatedAt(updatedCustomer.getCreatedAt());
            responseDTO.setUpdatedAt(updatedCustomer.getUpdatedAt());
            // Set other fields as needed

            return new ApiResponse<>("Cập nhật thông tin thành công", responseDTO);
        } catch (Exception e) {
            return new ApiResponse<>("Cập nhật thông tin thất bại: " + e.getMessage(), null);
        }
    }
}

