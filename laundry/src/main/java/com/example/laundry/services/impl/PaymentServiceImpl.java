package com.example.laundry.services.impl;

import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.Payment;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.PaymentRepository;
import com.example.laundry.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    public PaymentServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentResponse getPaymentInfo(PaymentRequest paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId().intValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!!!"));

        return PaymentResponse.builder()
                .orderId(Long.valueOf(order.getId().toString()))
                .totalAmount(order.getTotalAmount())
                .accountHolder("Pham Minh Chi")
                .bankName("MB Bank")
                .accountNumber("0943869063")
                .build();
    }

//    @Override
//    public Payment processPayment(Payment payment) {
//        paymentRepository.processPayment(payment);
//        return payment;
//    }
//
//    @Override
//    public void generateReceipt(Payment payment) {
//        paymentRepository.generateReceipt(payment);
//    }
}
