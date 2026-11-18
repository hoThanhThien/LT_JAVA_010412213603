package com.example.laundry.models.notification;

import com.example.laundry.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(name = "device_info", length = 255)
  private String deviceInfo;

  @Column(name = "ip_address", length = 64)
  private String ipAddress;

  @Column(name = "created_at")
  private Instant createdAt;

  public RefreshToken(Long id, String token, User user, Instant expiresAt) {
    this.id = id;
    this.token = token;
    this.user = user;
    this.expiresAt = expiresAt;
    this.createdAt = Instant.now();
  }
}
