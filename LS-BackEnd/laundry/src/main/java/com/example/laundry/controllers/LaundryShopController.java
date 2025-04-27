package com.example.laundry.controllers;

import com.example.laundry.dto.EmployeeDTO;
import com.example.laundry.dto.LaundryShopDTO;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.services.LaundryShopService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class LaundryShopController {
  @Autowired
  private LaundryShopService laundryShopService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<LaundryShopDTO>>> getAllShops() {
    ApiResponse<List<LaundryShopDTO>> shops = laundryShopService.getAllShop();
    return ResponseEntity.ok(shops);
  }
}
