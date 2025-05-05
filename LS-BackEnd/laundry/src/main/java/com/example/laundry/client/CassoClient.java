package com.example.laundry.client;

import com.example.laundry.config.CassoProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CassoClient {
  @Autowired
  private RestTemplate restTemplate;
  private final CassoProperties cassoProperties;

  public CassoTransactionResponse getTransactions(int page, int size) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Apikey " + cassoProperties.getApiKey());
    headers.setContentType(MediaType.APPLICATION_JSON);

    String url = cassoProperties.getBaseUrl() + "?page=" + page + "&pageSize=" + size + "&sort=desc";

    HttpEntity<Void> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<CassoTransactionResponse> response = restTemplate.exchange(
              url, HttpMethod.GET, request, CassoTransactionResponse.class);
      return response.getBody();
    }
    catch (Exception e) {
      log.error("Error fetching transactions from Casso API", e);
      throw new RuntimeException("Failed to fetch transactions from Casso API", e);
    }
  }

  @Data
  public static class CassoTransactionResponse {
    private int error;
    private String message;
    private CassoTransactionData data;

  }

  @Data
  public static class CassoTransactionData {
    private List<CassoTransaction> records;
  }

  @Data
  @Getter
  public static class CassoTransaction {
    private String id;
    @JsonProperty("tid")
    private String transactionId;
    private String description;
    private Double amount;
    @JsonProperty("bank_sub_acc_id")
    private String bankSubAccountId;
    @JsonProperty("when")
    private LocalDateTime transactionTime;
  }
}
