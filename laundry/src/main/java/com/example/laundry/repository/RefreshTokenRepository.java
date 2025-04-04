package com.example.laundry.repository;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  void deleteByUser(User user);
  void deleteByToken(String token);
}
