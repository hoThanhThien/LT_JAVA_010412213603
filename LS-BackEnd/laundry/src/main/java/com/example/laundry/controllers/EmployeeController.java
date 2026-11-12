package com.example.laundry.controllers;

import com.example.laundry.dto.OrderResponse;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.security.SecurityUtils;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;
  @Autowired
  private EmployeeRepository employeeRepository;

  // Lấy employee hiện tại
  private Employee getCurrentEmployee() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return SecurityUtils.resolveCurrentUser(authentication, employeeRepository::findById, employeeRepository::findByUsername);
  }

  @PutMapping("/{orderId}/status")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long orderId,
                                                       @RequestParam OrderStatus status) {
    Employee employee = getCurrentEmployee();
    if (employee == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new ApiResponse<>("Không tìm thấy thông tin Employee", null));
    }

    ApiResponse response = employeeService.updateOrderStatus(orderId, status, employee);
    if (response.getData() == null) {
      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(response);
  }

  @GetMapping("/orders")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<PagedResponse<OrderResponse>> getOrders(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    try {
      Employee employee = getCurrentEmployee();
      if (employee == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new PagedResponse<>("Không tìm thấy thông tin Employee", null));
      }
      PagedResponse<OrderResponse> response = employeeService.getOrders(employee, page, size);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
              .body(new PagedResponse<>("Lấy danh sách đơn hàng thất bại: " + e.getMessage(), null));
    }
  }
}
