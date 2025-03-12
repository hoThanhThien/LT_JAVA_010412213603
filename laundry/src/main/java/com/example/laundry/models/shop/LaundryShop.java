package com.example.laundry.models.shop;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="laudry_shops")
@DiscriminatorValue("LAUNDRYSHOP")
public class LaundryShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column
    private String description;

    @Column(name = "average_rating")
    private double averageRating;

    @Column(name = "is_verified")
    private boolean isVerified = false;
}
