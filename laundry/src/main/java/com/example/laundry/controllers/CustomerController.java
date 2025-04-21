package com.example.laundry.controllers;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.security.JwtUtil;
import com.example.laundry.services.CustomerService;
import com.example.laundry.services.RefreshTokenService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return customerRepository.findByUsername(currentUsername);
    }

    //order cho customer
    @PostMapping("/{customerId}/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> bookService(
            @PathVariable UUID customerId,
            @RequestBody OrderDTO orderDTO) {

        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
            ApiResponse<OrderResponse> newOrder = customerService.bookService(customer, orderDTO);
            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Đặt hàng thất bại: " + e.getMessage(), null));
        }
    }
    //order cho history customer
    @GetMapping("/{customerId}/history-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> historyOrder(@RequestHeader("Authorization") String authHeader) {
        try {
            //Kiem tra customer
            Customer customer = getCurrentCustomer();
            if(customer == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Không tìm thấy thông tin customer"));
            }

            ApiResponse<List<OrderResponse>> response = customerService.historyOrder(customer, null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Lấy lịch sử đơn hàng thất bại: " + e.getMessage(), null));
        }
    }
    // New endpoint for updating customer profile
    @PutMapping("/accounts/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CustomerProfileDTO>> updateProfile(@RequestBody CustomerProfileDTO profileDTO) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Không tìm thấy thông tin customer"));
            }

            ApiResponse<CustomerProfileDTO> response = customerService.updateCustomerProfile(customer, profileDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Cập nhật thông tin thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/orders/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PagedResponse<OrderResponse>> historyOrder(
            @RequestParam(defaultValue = "0", name = "paged") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new PagedResponse<>("Không tìm thấy thông tin khách hàng", null));
            }

            PagedResponse<OrderResponse> response = customerService.historyOrder(customer, page, size);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy lịch sử đơn hàng thất bại: " + e.getMessage(), null));
        }
    }



    // Simplified endpoint for creating orders
    @PostMapping("/orders/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Customer customer = getCurrentCustomer();
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Không tìm thấy thông tin customer"));
            }

            ApiResponse<OrderResponse> response = customerService.bookService(customer, orderDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Tạo đơn hàng thất bại: " + e.getMessage(), null));
        }
    }
    @GetMapping("/orders/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable String status) {
        try {
            CustomerController adminService = null;
            ApiResponse<List<OrderResponse>> response = adminService.getOrdersByStatus(status).getBody();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Lấy danh sách đơn hàng theo trạng thái thất bại: " + e.getMessage(), null));
        }
    }
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@RequestBody CustomerDTO customerDTO){
//        //Kiểm tra dữ liệu
//        if(customerDTO.getPassword() == null || customerDTO.getPassword().isEmpty()) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>("Password không được để trống", null));
//        }
//
//        //Kiểm tra password
//        if(!UserValidator.isValidPassword(customerDTO.getPassword())) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>("Password không hợp lệ!!!", null));
//        }
//
//        //Kiểm tra email
//      if(!UserValidator.isValidEmail(customerDTO.getEmail())) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>("Email không hợp lệ!!!", null));
//        }
//
//        if(customerRepository.existsByEmail(customerDTO.getEmail())) {
//           return ResponseEntity.badRequest()
//                   .body(new ApiResponse<>("Email đã tồn tại!!!", null));
//       }
//
//        //Kiểm tra phone
//        if(!UserValidator.isValidPhone(customerDTO.getPhone())) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>("Phone phải có 10 chữ số!!!", null));
//        }
//
//        if(customerRepository.existsByPhone(customerDTO.getPhone())) {
//            return ResponseEntity.badRequest()
//                    .body(new ApiResponse<>("Phone đã tồn tại!!!", null));
//        }
//
//        //Mã hóa password trước khi lưu xuống db
//        String encodedPassword = passwordEncoder.encode(customerDTO.getPassword());
//
//        Customer customer = new Customer();
//        customer.setUsername(customerDTO.getUsername());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setPhone(customerDTO.getPhone());
//        customer.setAddress(customerDTO.getAddress());
//        customer.setRoles(Roles.Customer);
//        customer.setPassword(encodedPassword);
//
//        Customer savedCustomer = customerService.addCustomer(customer);
//
//        //Tạo access token
//        String accessToken = jwtUtil.generateAccessToken(savedCustomer.getUsername());
//
//        //Tạo refresh token
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedCustomer);
//        String refreshTokenString = refreshToken.getToken();
//
//        CustomerResponseDTO.AccountInfo accountInfo = new CustomerResponseDTO.AccountInfo(
//                savedCustomer.getId(),
//                savedCustomer.getUsername(),
//                savedCustomer.getEmail(),
//                savedCustomer.getPhone(),
//                savedCustomer.getAddress(),
//                Roles.Customer
//        );
//
//        CustomerResponseDTO responseDTO = new CustomerResponseDTO(
//          accessToken,
//          refreshTokenString,
//          accountInfo
//        );
//
//        return ResponseEntity
//                .ok(new ApiResponse<>("Đăng ký thành công!!!", responseDTO));
//    }
}
