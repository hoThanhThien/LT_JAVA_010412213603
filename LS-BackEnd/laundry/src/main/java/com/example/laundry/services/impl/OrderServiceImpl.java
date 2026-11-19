package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với id: " + id));
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = findById(orderId);
        OrderStatus currentStatus = order.getOrderStatus();
        if (currentStatus != null && !currentStatus.canTransitionTo(status)) {
            throw new IllegalArgumentException(
                    "Không thể chuyển trạng thái đơn hàng từ " + currentStatus.name() + " sang " + status
            );
        }
        order.setOrderStatus(status);
        order.setUpdatedAt(new Date());
        orderRepository.save(order);
    }

    @Override
    public void notifyCustomers(Long customerId, String message) {
        log.info("Order notification dispatched to customerId={}: {}", customerId, message);
    }
}
