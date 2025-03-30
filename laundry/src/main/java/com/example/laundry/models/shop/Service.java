package com.example.laundry.models.shop;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="services")
@DiscriminatorValue("SERVICE")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column
    private String description;

    @Column(name = "estimated_time")
    private String estimatedTime;

    @ManyToOne
    private LaundryShop shop;

    public Service(String name, double price, String description, String estimatedTime) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.estimatedTime = estimatedTime;
    }
}
