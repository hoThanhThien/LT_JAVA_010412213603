package com.example.laundry.repository;

import com.example.laundry.models.notification.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
  long deleteByExpiresAtBefore(Instant time);
}

