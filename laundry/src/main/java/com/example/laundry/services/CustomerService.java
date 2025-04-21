package com.example.laundry.services;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;
import com.example.laundry.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CustomerService extends UserService{
    Customer addCustomer(Customer customer);
    CustomerResponseDTO register(RegisterRequest registerRequest);
    ApiResponse<OrderResponse> bookService(Customer customer, OrderDTO orderDTO);
    ApiResponse<List<OrderResponse>> historyOrder(Customer customer, OrderDTO orderDTO);
    ApiResponse<CustomerProfileDTO> updateCustomerProfile(Customer customer, CustomerProfileDTO profileDTO);

    ApiResponse<List<OrderResponse>> getOrdersByStatus(String status);

    PagedResponse<OrderResponse> getOrdersByStatus(String status, int page, int size);

    PagedResponse<OrderResponse> historyOrder(Customer customer, int page, int size);

    PagedResponse<OrderResponse> historyOrder(UUID customerId, int page, int size);


    //    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchShops(Customer customer, String location);
}
