package com.example.laundry.services;


import com.example.laundry.dto.ServiceDTO;
import com.example.laundry.utils.ApiResponse;

import java.util.List;

public interface ServiceService {
  ApiResponse<List<ServiceDTO>> getServicesByCategoryId(Integer shopId, Integer categoryId);
}
