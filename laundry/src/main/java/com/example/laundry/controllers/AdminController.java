package com.example.laundry.controllers;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.OrderResponse;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.dto.StoreOwnerDTO;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.utils.ApiResponse;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllEmployees(page, size));
    }
    @PostMapping("/storeowner/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StoreOwner>> createStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO) {
        ApiResponse<StoreOwner> response = adminService.createStoreOwner(storeOwnerDTO);

        if (response.getData() == null) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/storeowner/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO) {
        ApiResponse<String> response = adminService.removeStoreOwner(storeOwnerDTO);

        if (response.getData() == null) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/storeowners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<StoreOwnerDTO>> getAllStoreOwners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllStoreOwners(page, size));
    }
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        try {
            ApiResponse<List<OrderResponse>> response = adminService.getAllOrders();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Lấy danh sách đơn hàng thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/orders/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByCustomer(@PathVariable UUID customerId) {
        try {
            ApiResponse<List<OrderResponse>> response = adminService.getOrdersByCustomer(customerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Lấy danh sách đơn hàng của khách hàng thất bại: " + e.getMessage(), null));
        }
    }
}