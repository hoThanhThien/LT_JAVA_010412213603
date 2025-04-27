package com.example.laundry.controllers;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.LaundryShopService;
import com.example.laundry.utils.ApiResponse;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private LaundryShopService laundryShopService;
    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @PostMapping("/storeowner/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StoreOwner>> createStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO) {
        ApiResponse<StoreOwner> response = adminService.createStoreOwner(storeOwnerDTO);

        if (response.getData() == null) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/storeowner/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StoreOwnerDTO>> updateStoreOwner(@RequestBody StoreOwnerDTO storeOwnerDTO) {
        ApiResponse<StoreOwnerDTO> response = adminService.updateStoreOwner(storeOwnerDTO);

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

//    @GetMapping("/storeowners")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<PagedResponse<StoreOwnerDTO>> getAllStoreOwners(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(adminService.getAllStoreOwners(page, size));
//    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PagedResponse<EmployeeDTO> response = employeeService.getAllEmployees(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách nhân viên thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/storeowners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<StoreOwnerWithEmployeeDTO>> getAllEmployee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PagedResponse<StoreOwnerWithEmployeeDTO> response = adminService.getAllEmployeesBelongToStoreOwner(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách nhân viên thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<CustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllCustomers(page, size));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PagedResponse<OrderResponse> response = adminService.getAllOrders(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách đơn hàng thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/orders/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            PagedResponse<OrderResponse> response = adminService.getOrdersByCustomer(customerId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách đơn hàng của khách hàng thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/orders/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PagedResponse<OrderResponse> response = adminService.getOrdersByStatus(status, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách đơn hàng theo trạng thái thất bại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/shops")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<LaundryShopDTO>> getAllShops(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PagedResponse<LaundryShopDTO> response = laundryShopService.getAllShops(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new PagedResponse<>("Lấy danh sách nhân viên thất bại: " + e.getMessage(), null));
        }
    }
}