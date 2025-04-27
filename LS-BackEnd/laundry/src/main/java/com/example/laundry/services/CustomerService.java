package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.Customer;
import com.example.laundry.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService extends UserService{
    Customer addCustomer(Customer customer);
    CustomerResponseDTO register(RegisterRequest registerRequest);
    ApiResponse<OrderResponse> bookService(Customer customer, OrderDTO orderDTO);
    ApiResponse<CustomerDTO> updateCustomer(Customer customer, CustomerDTO customerDTO);
    PagedResponse<OrderResponse> historyOrder(Customer customer, int page, int size);
}
