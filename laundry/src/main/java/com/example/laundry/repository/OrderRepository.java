package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findOrdersByCustomerId(UUID customerId);
    List<Order> findByOrderStatus(OrderStatus status);
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.orderStatus = :status")
    List<Order> findOrdersByCustomerIdAndStatus(@Param("customerId") UUID customerId, @Param("status") OrderStatus status);
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersOrderByCreatedAtDesc();
//    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);
}
