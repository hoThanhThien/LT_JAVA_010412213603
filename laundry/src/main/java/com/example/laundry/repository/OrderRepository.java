package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findOrdersByCustomerId(UUID customerId);
//    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);
}
