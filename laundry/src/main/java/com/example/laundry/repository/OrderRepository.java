package com.example.laundry.repository;

import com.example.laundry.dto.OrderResponse;
import com.example.laundry.dto.PagedResponse;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.order.PaymentStatus;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.StoreOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC")
    Page<Order> findByCustomer_Id(UUID customerId, Pageable pageable);
    Page<Order> findOrdersByLaundryShop(LaundryShop laundryShop, Pageable pageable);
    Page<Order> findOrderByOrderStatus(OrderStatus orderStatus, Pageable pageable);
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
}
