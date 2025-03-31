package com.example.laundry.services;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;

import java.util.List;

public interface CustomerService extends UserService{
    void addCustomer(Customer customer);
//    Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions);
//    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchShops(Customer customer, String location);
}
