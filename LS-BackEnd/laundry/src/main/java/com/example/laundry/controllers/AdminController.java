package com.example.laundry.controllers;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.services.AdminService;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.LaundryShopService;
import com.example.laundry.utils.ApiResponse;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LaundryShopService laundryShopService;

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
    public ResponseEntity<ApiResponse<String>> deleteStoreOwner(
            @RequestBody(required = false) StoreOwnerDTO storeOwnerDTO,
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String username) {
        StoreOwnerDTO effectiveDto = storeOwnerDTO != null ? storeOwnerDTO : new StoreOwnerDTO();
        if (effectiveDto.getId() == null && id != null) {
            effectiveDto.setId(id);
        }
        if ((effectiveDto.getEmail() == null || effectiveDto.getEmail().isBlank()) && email != null) {
            effectiveDto.setEmail(email);
        }
        if ((effectiveDto.getPhone() == null || effectiveDto.getPhone().isBlank()) && phone != null) {
            effectiveDto.setPhone(phone);
        }
        if ((effectiveDto.getUsername() == null || effectiveDto.getUsername().isBlank()) && username != null) {
            effectiveDto.setUsername(username);
        }

        ApiResponse<String> response = adminService.removeStoreOwner(effectiveDto);

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
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }

    @GetMapping("/storeowners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<StoreOwnerWithEmployeeDTO>> getAllEmployee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllEmployeesBelongToStoreOwner(page, size));
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
        return ResponseEntity.ok(adminService.getAllOrders(page, size));
    }

    @GetMapping("/orders/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(adminService.getOrdersByCustomer(customerId, page, size));
    }

    @GetMapping("/orders/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getOrdersByStatus(status, page, size));
    }

    @GetMapping("/shops")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<LaundryShopDTO>> getAllShops(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(laundryShopService.getAllShops(page, size));
    }
}