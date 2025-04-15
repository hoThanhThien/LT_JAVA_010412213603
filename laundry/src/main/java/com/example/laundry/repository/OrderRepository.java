package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);
}
//public interface OrderRepository extends JpaRepository<Order,Integer> {
    //    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);

