package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.User;
import com.example.laundry.services.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions) {
        return null;
    }

    @Override
    public void trackOrder(Customer customer, Order order) {

    }

    @Override
    public void makePayment(Customer customer, Order order, String paymentMethod, double amount) {

    }

    @Override
    public List<Order> getOrderHistory(Customer customer) {
        return List.of();
    }

    @Override
    public List<LaundryShop> searchShops(Customer customer, String location) {
        return List.of();
    }
}
