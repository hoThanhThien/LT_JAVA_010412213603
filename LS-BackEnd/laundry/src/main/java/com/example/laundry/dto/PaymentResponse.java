package com.example.laundry.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponse {
  private Long orderId;
  private Double totalAmount;
  private String accountHolder;
  private String bankName;
  private String accountNumber;
  private String transferContent;
  private String paymentStatus;
  private String qrCode;
}
