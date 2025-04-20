package com.example.laundry.services.impl;

import com.example.laundry.dto.OrderDTO;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order findById(Integer id) {

        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = findById(orderId);
        order.setOrderStatus(status);
        orderRepository.save(order);

    }

    @Override
    public void notifyCustomers(Long CustomerId, String message) {

    }

}
