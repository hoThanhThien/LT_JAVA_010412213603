package com.example.laundry.services;

import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.utils.ApiResponse;

public interface PaymentService {
  PaymentResponse getPaymentInfo(PaymentRequest paymentRequest);
  ApiResponse<Object> checkPaymentStatus(Long orderId);
//  ApiResponse<Object> verifyPaymentFromBank(Long orderId);
  void pollBankTransactions();
}
