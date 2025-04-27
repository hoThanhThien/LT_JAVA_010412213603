package com.example.laundry.controllers;

import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.services.ServiceCategoryService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ServiceCategoryController {
  @Autowired
  private ServiceCategoryService serviceCategoryService;

  @GetMapping("/{shopId}/categories")
  public ResponseEntity<ApiResponse<List<ServiceCategoryDTO>>> getServiceCategoriesByShopId(@PathVariable Integer shopId) {
    ApiResponse<List<ServiceCategoryDTO>> serviceCategories = serviceCategoryService.getServiceCategoriesByShopId(shopId);

    return ResponseEntity.ok(serviceCategories);
  }
}
