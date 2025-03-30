package com.example.laundry.services;

import com.example.laundry.models.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createOrder(Order order);
    void deleteOrderById(Long id);
    Order updateOrderStatus(Long orderId, String status);
    void notifyCustomer(Long orderId, String message);

}