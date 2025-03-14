package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions) {
        return customerRepository.bookOrder(customer, laundryShop, service);
    }

    @Override
    public void trackOrder(Customer customer, Order order) {

    }

    @Override
    public void makePayment(Customer customer, Order order, String paymentMethod, double amount) {

    }

    @Override
    public List<Order> getOrderHistory(Customer customer) {
        return null;
    }

    @Override
    public List<LaundryShop> searchShops(Customer customer, String location) {
        return null;
    }
}
