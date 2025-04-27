package com.example.laundry.services;

import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.utils.ApiResponse;

import java.util.List;

public interface ServiceCategoryService {
  ApiResponse<List<ServiceCategoryDTO>> getServiceCategoriesByShopId(Integer shopId);
}
