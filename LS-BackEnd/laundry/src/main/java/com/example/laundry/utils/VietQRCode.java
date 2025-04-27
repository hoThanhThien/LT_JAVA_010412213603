package com.example.laundry.utils;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class VietQRCode {
  @Value("${viet-qr-url}")
  private String vietQRUrl;

  @Value("${account-holder}")
  private String accountHolder;

  public String generateQRCode(String bankCode, String accountNumber, String totalAmount, String transferContent, String accountHolder) {
    try {
      String encodedContent = URLEncoder.encode(transferContent, StandardCharsets.UTF_8);
      String encodedAccountHolder = URLEncoder.encode(accountHolder, StandardCharsets.UTF_8);
      String cleanAmount = totalAmount.replace(".0", "");

      return String.format("%s/%s-%s-compact.png?amount=%s&addInfo=%s&accountName=%s",
              vietQRUrl, bankCode, accountNumber, cleanAmount, encodedContent, encodedAccountHolder);
    } catch (Exception e) {
      throw new RuntimeException("Error generating QR code", e);
    }
  }
}

