package com.example.laundry.services.impl;

import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.repository.ServiceCategoryRepository;
import com.example.laundry.services.ServiceCategoryService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {
  @Autowired
  private ServiceCategoryRepository serviceCategoryRepository;

  @Override
  public ApiResponse<List<ServiceCategoryDTO>> getServiceCategoriesByShopId(Integer shopId) {
    List<ServiceCategory> categories = serviceCategoryRepository.findByShopId(shopId);

    List<ServiceCategoryDTO> categoriesDTOs = categories.stream()
            .map(serviceCategory -> new ServiceCategoryDTO(
                    serviceCategory.getId(),
                    serviceCategory.getName(),
                    serviceCategory.getImageDesc(),
                    serviceCategory.getDescription()
            ))
            .toList();

    return new ApiResponse<>("Danh sách các mục dịch vụ", categoriesDTOs);
  }
}
