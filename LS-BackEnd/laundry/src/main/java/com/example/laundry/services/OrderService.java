package com.example.laundry.services;

import com.example.laundry.dto.OrderDTO;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;

import java.util.List;

public interface OrderService {
    Order findById(Long id);
    Order save(Order order);
    void updateOrderStatus(Long orderId, OrderStatus status);
    void notifyCustomers(Long CustomerId, String message);
}
