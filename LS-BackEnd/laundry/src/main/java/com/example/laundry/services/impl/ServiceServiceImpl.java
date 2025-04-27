package com.example.laundry.services.impl;

import com.example.laundry.dto.ServiceCategoryDTO;
import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.models.shop.Service;
import com.example.laundry.repository.ServiceRepository;
import com.example.laundry.services.ServiceService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
  @Autowired
  private ServiceRepository serviceRepository;

  @Override
  public ApiResponse<List<ServiceDTO>> getServicesByCategoryId(Integer shopId, Integer categoryId) {
    List<Service> services = serviceRepository.findByCategoryIdAndCategoryShopId(shopId, categoryId);

    List<ServiceDTO> servicesDTOs = services.stream()
            .map(service -> new ServiceDTO(
                    service.getId(),
                    service.getName(),
                    service.getDescription(),
                    service.getEstimatedTime(),
                    service.getImageDesc(),
                    service.getPrice()
            ))
            .toList();

    return new ApiResponse<>("Danh sách các dịch vụ", servicesDTOs);
  }
}
