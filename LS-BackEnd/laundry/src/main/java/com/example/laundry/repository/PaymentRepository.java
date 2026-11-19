package com.example.laundry.repository;

import com.example.laundry.models.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
  Optional<Payment> findByOrderId(Long orderId);
  boolean existsByExternalTransactionId(String externalTransactionId);
}
