package com.example.laundry.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {
  private final int maxAttempts;
  private final long windowSeconds;

  private record AttemptState(int failedCount, Instant windowStart) {}

  private final Map<String, AttemptState> attempts = new ConcurrentHashMap<>();

  public LoginRateLimiter(
          @Value("${security.login.max-attempts:5}") int maxAttempts,
          @Value("${security.login.window-seconds:60}") long windowSeconds) {
    this.maxAttempts = maxAttempts;
    this.windowSeconds = windowSeconds;
  }

  public void checkAllowed(String key) {
    if (key == null || key.isBlank()) {
      return;
    }
    Instant now = Instant.now();
    AttemptState state = attempts.get(key);
    if (state == null) {
      return;
    }
    if (state.windowStart().plusSeconds(windowSeconds).isBefore(now)) {
      attempts.remove(key, state);
      return;
    }
    if (state.failedCount() >= maxAttempts) {
      throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,
              "Quá nhiều lần đăng nhập thất bại. Vui lòng thử lại sau " + windowSeconds + " giây."
      );
    }
  }

  public void recordFailure(String key) {
    if (key == null || key.isBlank()) {
      return;
    }
    Instant now = Instant.now();
    attempts.compute(key, (k, current) -> {
      if (current == null || current.windowStart().plusSeconds(windowSeconds).isBefore(now)) {
        return new AttemptState(1, now);
      }
      return new AttemptState(current.failedCount() + 1, current.windowStart());
    });
  }

  public void recordSuccess(String key) {
    if (key != null && !key.isBlank()) {
      attempts.remove(key);
    }
  }

  public void resetAll() {
    attempts.clear();
  }
}
