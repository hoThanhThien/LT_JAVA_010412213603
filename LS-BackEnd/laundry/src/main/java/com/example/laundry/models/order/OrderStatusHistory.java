package com.example.laundry.models.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "from_status", length = 32)
  private OrderStatus fromStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "to_status", nullable = false, length = 32)
  private OrderStatus toStatus;

  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "changed_by_user_id", columnDefinition = "VARCHAR(36)")
  private UUID changedByUserId;

  @Column(name = "changed_by_username", length = 100)
  private String changedByUsername;

  @Column(length = 255)
  private String note;

  @Column(name = "changed_at", nullable = false)
  private Date changedAt;
}
