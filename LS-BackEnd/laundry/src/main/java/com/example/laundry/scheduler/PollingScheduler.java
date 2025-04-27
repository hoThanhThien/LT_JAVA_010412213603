package com.example.laundry.scheduler;

import com.example.laundry.config.CassoProperties;
import com.example.laundry.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class PollingScheduler {
  private final PaymentService paymentService;
  private final CassoProperties cassoProperties;

  @Scheduled(fixedDelayString = "${casso.polling-interval-ms:10000}")
  public void pollTransactions() {
    log.debug("Starting scheduled transaction polling");
    paymentService.pollBankTransactions();
  }
}