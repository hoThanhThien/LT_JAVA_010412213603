package com.example.laundry.repository;

import com.example.laundry.models.order.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {
    Payment processPayment(Payment payment);
    void generateReceipt(Payment payment);
}
