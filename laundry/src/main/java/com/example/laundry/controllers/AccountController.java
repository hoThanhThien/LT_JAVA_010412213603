package com.example.laundry.controllers;

import com.example.laundry.dto.CustomerProfileDTO;
import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.services.AccountService;
import com.example.laundry.services.CustomerService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@RestController
public class AccountController {
  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private AccountService accountService;
  private Customer getCurrentCustomer() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    return customerRepository.findByUsername(currentUsername);
  }

  @GetMapping("/accounts/me")
  public ResponseEntity<GetInfoResponse> getAccountInfo(@RequestHeader("Authorization") String authHeader) {
    String token = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
    }

    GetInfoRequest getInfoRequest = new GetInfoRequest(token);
    GetInfoResponse response = accountService.getInfo(getInfoRequest);
    return ResponseEntity.ok(response);
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
}
