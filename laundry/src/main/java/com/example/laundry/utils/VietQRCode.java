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

  public String generateQRCode(String bankCode, String accountNumber, String totalAmount, String transferContent) {
    try {
      String encodedContent = URLEncoder.encode(transferContent, StandardCharsets.UTF_8);

      return String.format("%s/%s/%s/%s?amount=%s&addInfo=%s&accountName=Pham%%20Minh%%20Chi",
              vietQRUrl, bankCode, accountNumber, "compact", totalAmount, encodedContent);
    }
    catch (Exception e) {
      throw new RuntimeException("Error encoding QR content", e);
    }
  }
}
