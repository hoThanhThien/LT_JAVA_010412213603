package com.example.laundry.controllers;

import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.services.ServiceService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop/{shopId}/category")
public class ServiceController {
  @Autowired
  private ServiceService serviceService;

  @GetMapping("/{categoryId}/services")
  public ResponseEntity<ApiResponse<List<ServiceDTO>>> getServicesByCategory(@PathVariable Integer categoryId, @PathVariable Integer shopId) {
    ApiResponse<List<ServiceDTO>> serviceCategories = serviceService.getServicesByCategoryId(categoryId, shopId);

    return  ResponseEntity.ok(serviceCategories);
  }
}
