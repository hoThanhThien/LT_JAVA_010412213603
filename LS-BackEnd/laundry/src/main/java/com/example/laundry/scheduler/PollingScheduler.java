package com.example.laundry.scheduler;

import com.example.laundry.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(name = "casso.polling.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class PollingScheduler {
  private final PaymentService paymentService;

  @Scheduled(fixedDelayString = "${casso.polling-interval-ms:10000}")
  public void pollTransactions() {
    log.debug("Starting scheduled transaction polling");
    try {
      paymentService.pollBankTransactions();
    } catch (Exception e) {
      log.warn("Scheduled bank transaction polling failed: {}", e.getMessage());
    }
  }
}