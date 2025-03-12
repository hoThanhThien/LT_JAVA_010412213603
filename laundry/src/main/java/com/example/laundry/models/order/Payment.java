package com.example.laundry.models.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @Column(name="payment_method", nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status = "PENDING";

    @CreationTimestamp
    @Column(name="payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @UpdateTimestamp
    @Column(name="update_at")
    private LocalDateTime updateAt;

    public Payment(Long id, Order order, String paymentMethod, double amount) {
        this.id = id;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }
}
