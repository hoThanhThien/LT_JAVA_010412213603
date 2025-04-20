package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.user.Customer;
import com.example.laundry.utils.ApiResponse;

import java.util.List;

public interface CustomerService extends UserService{
    Customer addCustomer(Customer customer);
    CustomerResponseDTO register(RegisterRequest registerRequest);
    ApiResponse<OrderResponse> bookService(Customer customer, OrderDTO orderDTO);
    ApiResponse<List<OrderResponse>> historyOrder(Customer customer, OrderDTO orderDTO);
//    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchShops(Customer customer, String location);
}
