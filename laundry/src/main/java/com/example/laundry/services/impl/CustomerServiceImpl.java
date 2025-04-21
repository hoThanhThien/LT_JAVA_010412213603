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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            customer.setPhone(verifiedPhone);
            customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            customer.setUsername(registerRequest.getUsername());
            customer.setRoles(Roles.Customer);

            Customer savedCustomer = customerRepository.save(customer);
            //Tạo access token
            String accessToken = jwtUtil.generateAccessToken(savedCustomer.getUsername());

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
        order.setCustomer(customer);
        order.setLaundryShop(laundryShop);
        order.setServiceCategory(serviceCategory);
        order.setService(service);
        order.setOrderVolume(orderDTO.getOrderVolume());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setInstructions(orderDTO.getInstructions());
        double totalAmount = orderDTO.getOrderVolume() * service.getPrice();
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderService.save(order);

        OrderResponse responseDTO = new OrderResponse();
        responseDTO.setId(savedOrder.getId());
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

        return new ApiResponse<>("Bạn đã ta đơn giặt hàng thành công. Vui lòng chú ý thông báo của chúng tôi!", responseDTO);
    }

    @Override
    public ApiResponse<List<OrderResponse>> historyOrder(Customer customer, OrderDTO orderDTO) {
        return null;
    }

    public PagedResponse<OrderResponse> historyOrder(Customer customer, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByCustomerId(customer.getId(), pageable);

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

    // New method for updating customer profile
    @Override
    @Transactional
    public ApiResponse<CustomerProfileDTO> updateCustomerProfile(Customer customer, CustomerProfileDTO profileDTO) {
        try {
            // Validate email if provided
            if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
                if (customerRepository.existsByEmail(profileDTO.getEmail()) &&
                        !profileDTO.getEmail().equals(customer.getEmail())) {
                    return new ApiResponse<>("Email đã tồn tại trong hệ thống", null);
                }
                customer.setEmail(profileDTO.getEmail());
            }

            // Validate phone if provided
            if (profileDTO.getPhone() != null && !profileDTO.getPhone().isEmpty() &&
                    !profileDTO.getPhone().equals(customer.getPhone())) {
                if (customerRepository.existsByPhone(profileDTO.getPhone())) {
                    return new ApiResponse<>("Số điện thoại đã tồn tại trong hệ thống", null);
                }
                customer.setPhone(profileDTO.getPhone());
            }

            // Update other fields
            if (profileDTO.getUsername() != null && !profileDTO.getUsername().isEmpty()) {
                customer.setUsername(profileDTO.getUsername());
            }

            if (profileDTO.getAddress() != null) {
                customer.setAddress(profileDTO.getAddress());
            }

            // Save the updated customer
            Customer updatedCustomer = customerRepository.save(customer);

            // Build response
            CustomerProfileDTO responseDTO = new CustomerProfileDTO();
            responseDTO.setUsername(updatedCustomer.getUsername());
            responseDTO.setEmail(updatedCustomer.getEmail());
            responseDTO.setPhone(updatedCustomer.getPhone());
            responseDTO.setAddress(updatedCustomer.getAddress());
            // Set other fields as needed

            return new ApiResponse<>("Cập nhật thông tin thành công", responseDTO);
        } catch (Exception e) {
            return new ApiResponse<>("Cập nhật thông tin thất bại: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<OrderResponse>> getOrdersByStatus(String status) {
        return null;
    }

    @Override
    public PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size) {
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new PagedResponse<>("Trạng thái đơn hàng không hợp lệ. Các trạng thái hợp lệ: " + Arrays.toString(OrderStatus.values()), null);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> statusOrdersPage = (Page<Order>) orderRepository.findByOrderStatus(orderStatus, pageable);

        List<OrderResponse> responseList = mapOrdersToOrderResponses(statusOrdersPage.getContent());

        Meta meta = new Meta(
                statusOrdersPage.getNumber() + 1,
                statusOrdersPage.getSize(),
                statusOrdersPage.getTotalElements(),
                statusOrdersPage.getTotalPages()
        );
        PagedData<OrderResponse> pagedData = new PagedData<>(meta, responseList);

        return new PagedResponse<>("Lấy danh sách đơn hàng theo trạng thái thành công", pagedData);
    }
    @Override
    public PagedResponse<OrderResponse> historyOrder(UUID customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByCustomerId(customerId, pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    // set các field
                    return response;
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


//    @Override
//    public Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions) {
//        return customerRepository.bookOrder(customer, laundryShop, service);
//    }
//
//    @Override
//    public void trackOrder(Customer customer, Order order) {
//
//    }
//
//    @Override
//    public void makePayment(Customer customer, Order order, String paymentMethod, double amount) {
//
//    }
//
//    @Override
//    public List<Order> getOrderHistory(Customer customer) {
//        return null;
//    }
//
//    @Override
//    public List<LaundryShop> searchShops(Customer customer, String location) {
//        return null;
//    }

