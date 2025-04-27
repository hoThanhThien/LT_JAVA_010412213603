package com.example.laundry.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "casso")
public class CassoProperties {
  private String apiKey;
  private String baseUrl;
  private long pollingInterval;
}
