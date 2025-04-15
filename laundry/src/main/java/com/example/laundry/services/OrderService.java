package com.example.laundry.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.laundry.models.order.Order;

public interface OrderService {
    Page<Order> getPaginatedOrders(Pageable pageable);

    Page<Order> getAllOrders(Pageable pageable);

    //    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);
}

