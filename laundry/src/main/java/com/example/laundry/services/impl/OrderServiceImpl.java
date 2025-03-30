package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

//    @Override
//    public void findById(int id) {
//        orderRepository.findById(id);
//    }
//
//    @Override
//    public void save(Order order) {
//        orderRepository.save(order);
//    }
//
//    @Override
//    public void deleteById(int id) {
//        orderRepository.deleteById(id);
//    }
//
//    @Override
//    public void updateOrderStatus(int orderId, String status) {
//        orderRepository.updateOrderStatus(orderId, status);
//    }
//
//    @Override
//    public void notifyCustomer(int orderId, String message) {
//        orderRepository.notifyCustomer(orderId, message);
//    }
}
