package com.example.laundry.controllers;

import com.example.laundry.dto.PaymentRequest;
import com.example.laundry.dto.PaymentResponse;
import com.example.laundry.services.PaymentService;
import com.example.laundry.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:3000, https://oauth.casso.vn", allowCredentials = "true")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/info")
    public ResponseEntity<PaymentResponse> getPaymentInfo(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.getPaymentInfo(paymentRequest));
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<ApiResponse<Object>> checkPaymentStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.checkPaymentStatus(orderId));
    }

//    @GetMapping("/verify/{orderId}")
//    public ResponseEntity<ApiResponse<Object>> verifyPayment(@PathVariable Long orderId) {
//        return ResponseEntity.ok(paymentService.verifyPaymentFromBank(orderId));
//    }
}