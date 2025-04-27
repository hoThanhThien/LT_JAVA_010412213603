package com.example.laundry.repository;

import com.example.laundry.models.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
//    Payment processPayment(Payment payment);
//    void generateReceipt(Payment payment);
}
