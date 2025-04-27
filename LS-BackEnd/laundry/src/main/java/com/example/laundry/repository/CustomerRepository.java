package com.example.laundry.repository;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<Customer> findByPhone(String phone);

    Customer findByUsername(String currentUsername);
    //    Order bookOrder(Customer customer, LaundryShop laundryShop, Service service);
//    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchLaundryShop(Customer customer, String location);
}
