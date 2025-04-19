package com.example.laundry.controllers;

import com.example.laundry.dto.GetInfoRequest;
import com.example.laundry.dto.GetInfoResponse;
import com.example.laundry.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@RestController
public class AccountController {
  @Autowired
  private AccountService accountService;

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
}
