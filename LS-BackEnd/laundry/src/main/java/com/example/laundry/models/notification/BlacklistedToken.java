package com.example.laundry.models.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Access token đã logout. Chỉ lưu SHA-256 của token (không lưu token gốc)
 * và thời điểm token hết hạn để job dọn dẹp xóa đi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blacklisted_tokens", indexes = @Index(name = "idx_blacklisted_tokens_expires_at", columnList = "expiresAt"))
public class BlacklistedToken {
  @Id
  @Column(length = 64, nullable = false, updatable = false)
  private String tokenHash;

  @Column(nullable = false)
  private Instant expiresAt;
}

