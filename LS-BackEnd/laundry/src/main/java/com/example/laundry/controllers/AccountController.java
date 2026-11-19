package com.example.laundry.controllers;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {
  @Autowired
  private AccountService accountService;

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

  @org.springframework.web.bind.annotation.PostMapping("/accounts/change-password")
  public ResponseEntity<com.example.laundry.utils.ApiResponse<Void>> changePassword(
          org.springframework.security.core.Authentication authentication,
          @org.springframework.web.bind.annotation.RequestBody com.example.laundry.dto.ChangePasswordRequest request
  ) {
    return ResponseEntity.ok(accountService.changePassword(authentication, request));
  }
}
