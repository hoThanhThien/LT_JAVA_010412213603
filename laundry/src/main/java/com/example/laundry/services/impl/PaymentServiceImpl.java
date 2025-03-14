package com.example.laundry.services.impl;

import com.example.laundry.models.order.Payment;
import com.example.laundry.services.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public Payment processPayment(Payment payment) {
        return null;
    }

    @Override
    public void generateReceipt(Payment payment) {

    }
}
