package com.example.laundry.services.impl;

import com.example.laundry.models.order.Payment;
import com.example.laundry.repository.PaymentRepository;
import com.example.laundry.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Payment payment) {
        paymentRepository.processPayment(payment);
        return payment;
    }

    @Override
    public void generateReceipt(Payment payment) {
        paymentRepository.generateReceipt(payment);
    }
}
