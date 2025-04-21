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
}
