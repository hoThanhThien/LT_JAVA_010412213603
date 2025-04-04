package com.example.laundry.controllers;

import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.models.order.Payment;
import com.example.laundry.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponse> getPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentService.getPaymentInfo(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}
