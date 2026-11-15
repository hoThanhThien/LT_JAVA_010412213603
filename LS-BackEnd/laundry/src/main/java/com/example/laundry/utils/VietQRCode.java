package com.example.laundry.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class VietQRCode {
  @Value("${viet-qr-url}")
  private String vietQRUrl;

  public String generateQRCode(String bankCode, String accountNumber, String totalAmount, String transferContent, String accountHolder) {
    try {
      String encodedContent = URLEncoder.encode(transferContent != null ? transferContent : "", StandardCharsets.UTF_8);
      String encodedAccountHolder = URLEncoder.encode(accountHolder != null ? accountHolder : "", StandardCharsets.UTF_8);
      String cleanAmount = normalizeAmount(totalAmount);

      return String.format("%s/%s-%s-compact.png?amount=%s&addInfo=%s&accountName=%s",
              vietQRUrl, bankCode, accountNumber, cleanAmount, encodedContent, encodedAccountHolder);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Error generating QR code", e);
    }
  }

  private String normalizeAmount(String totalAmount) {
    if (totalAmount == null || totalAmount.isBlank()) {
      throw new IllegalArgumentException("Total amount must not be blank");
    }
    BigDecimal parsed = new BigDecimal(totalAmount.trim());
    if (parsed.compareTo(BigDecimal.ZERO) == 0) {
      return "0";
    }
    return parsed.stripTrailingZeros().toPlainString();
  }
}

