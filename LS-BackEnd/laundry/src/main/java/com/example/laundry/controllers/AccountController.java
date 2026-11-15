package com.example.laundry.controllers;

import com.example.laundry.dto.CustomerDTO;
import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.security.SecurityUtils;
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
    return SecurityUtils.resolveCurrentUser(authentication, customerRepository::findById, customerRepository::findByUsername);
  }

  @GetMapping("/accounts/me")
  public ResponseEntity<GetInfoResponse> getAccountInfo(
          @RequestHeader(value = "Authorization", required = false) String authHeader) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()
            && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
      return ResponseEntity.ok(accountService.getCurrentUserInfo(authentication));
    }

    String token = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
    }
    if (token == null || token.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GetInfoResponse(null, "Chưa đăng nhập"));
    }

    GetInfoResponse response = accountService.getInfo(new GetInfoRequest(token));
    return ResponseEntity.ok(response);
  }
}
