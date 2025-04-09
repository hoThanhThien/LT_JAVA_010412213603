package com.example.laundry.services;

import com.example.laundry.dto.CustomerResponseDTO;
import com.example.laundry.dto.RegisterRequest;
import com.example.laundry.dto.RegisterResponse;
import com.example.laundry.models.user.Customer;

public interface CustomerService extends UserService{
    Customer addCustomer(Customer customer);
    CustomerResponseDTO register(RegisterRequest registerRequest);
//    Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions);
//    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchShops(Customer customer, String location);
}
