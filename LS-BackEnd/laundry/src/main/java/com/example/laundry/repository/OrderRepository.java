package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.order.PaymentStatus;
import com.example.laundry.models.shop.LaundryShop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = {"customer", "laundryShop", "serviceCategory", "service"})
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "laundryShop", "serviceCategory", "service"})
    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "laundryShop", "serviceCategory", "service"})
    Page<Order> findByCustomer_Id(UUID customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "laundryShop", "serviceCategory", "service"})
    Page<Order> findOrdersByLaundryShop(LaundryShop laundryShop, Pageable pageable);

    boolean existsByLaundryShop(LaundryShop laundryShop);

    boolean existsByService(com.example.laundry.models.shop.Service service);

    @EntityGraph(attributePaths = {"customer", "laundryShop", "serviceCategory", "service"})
    Page<Order> findOrderByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);

    @Modifying
    @Query("UPDATE Order o SET o.paymentStatus = :newStatus, o.updatedAt = :updatedAt WHERE o.id = :orderId AND o.paymentStatus = :expectedStatus")
    int updatePaymentStatusIfMatches(@Param("orderId") Long orderId,
                                     @Param("expectedStatus") PaymentStatus expectedStatus,
                                     @Param("newStatus") PaymentStatus newStatus,
                                     @Param("updatedAt") Date updatedAt);
}
