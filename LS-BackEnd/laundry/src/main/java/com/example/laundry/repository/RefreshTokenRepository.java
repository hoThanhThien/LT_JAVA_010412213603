package com.example.laundry.repository;

import com.example.laundry.models.notification.RefreshToken;
import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  List<RefreshToken> findByUserOrderByExpiresAtDesc(User user);
  Optional<RefreshToken> findByIdAndUser(Long id, User user);
  void deleteByUser(User user);
  void deleteByToken(String token);
  void deleteByUserAndExpiresAtBefore(User user, Instant now);

  @Modifying
  @Query("DELETE FROM RefreshToken rt WHERE rt.token = :token")
  int deleteByTokenReturningCount(@Param("token") String token);
}
