package com.example.laundry.services.interfaces;


import com.example.laundry.models.order.Payment;

public interface PaymentProcessor {
    boolean processPayment(Payment payment);
    void generateReceipt(Payment payment);
}
