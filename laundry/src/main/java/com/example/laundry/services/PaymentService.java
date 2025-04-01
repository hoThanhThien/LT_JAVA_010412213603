package com.example.laundry.services;

import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;

public interface PaymentService {
  PaymentResponse getPaymentInfo(PaymentRequest paymentRequest);
//    Payment processPayment(Payment payment);
//    void generateReceipt(Payment payment);
}
