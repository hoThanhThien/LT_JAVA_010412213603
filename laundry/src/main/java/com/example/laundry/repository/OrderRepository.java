package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    void findById(int id);
    void save(Order order);
    void deleteById(int id);
    void updateOrderStatus(int orderId, String status);
    void notifyCustomer(int orderId, String message);
}
