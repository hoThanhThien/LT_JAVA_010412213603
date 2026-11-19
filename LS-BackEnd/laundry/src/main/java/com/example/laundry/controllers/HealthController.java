package com.example.laundry.controllers;

import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @GetMapping({"/health", "/actuator/health"})
  public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
    Map<String, Object> details = new LinkedHashMap<>();
    details.put("timestamp", Instant.now().toString());
    try {
      Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
      boolean dbUp = result != null && result == 1;
      details.put("status", dbUp ? "UP" : "DOWN");
      details.put("database", dbUp ? "UP" : "DOWN");
      if (!dbUp) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiResponse<>("DEGRADED", details));
      }
      return ResponseEntity.ok(new ApiResponse<>("OK", details));
    } catch (Exception ex) {
      details.put("status", "DOWN");
      details.put("database", "DOWN");
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
              .body(new ApiResponse<>("DOWN", details));
    }
  }
}
