package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.notification.EmailService;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(OrderStatus.valueOf(status));
            return orderRepository.save(order);
        }
        return null; // Hoặc ném một exception nếu không tìm thấy
    }

    @Override
    public void notifyCustomer(Long orderId, String message) {
        Optional<Order> orderOpt = getOrderById(orderId);
        if (orderOpt.isPresent() && orderOpt.get().getCustomer() != null) {
            String customerEmail = orderOpt.get().getCustomer().getEmail();
            emailService.sendEmail(customerEmail, message);
        }
    }
}