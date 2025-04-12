package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
<<<<<<< HEAD
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm các đơn hàng theo trạng thái
    List<Order> findByOrderStatus(OrderStatus status);

    // Tìm các đơn hàng của một khách hàng cụ thể
    List<Order> findByCustomer(Customer customer);

    // Tìm các đơn hàng của một khách hàng theo ID (cách khác)
    List<Order> findByCustomerId(Long customer_id);


}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
//    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
