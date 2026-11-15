package com.example.laundry.models.shop;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import com.example.laundry.models.order.Order;

@Data
@Builder
@EqualsAndHashCode(exclude = {"category", "orders"})
@ToString(exclude = {"category", "orders"})
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
    private Double price;

    @Column
    private String description;

    @Column(name = "estimated_time")
    private String estimatedTime;

    @Lob
    @Column(name = "image_desc", columnDefinition = "LONGTEXT")
    private String imageDesc;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ServiceCategory category;

    @Builder.Default
    @OneToMany(mappedBy = "service")
    private List<Order> orders = new ArrayList<>();

    public Service(String name, double price, String description, String estimatedTime) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.estimatedTime = estimatedTime;
    }

    public Service(Long id, String name, String description, String estimateTime, Double price, String imageDesc) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.estimatedTime = estimateTime;
        this.price = price;
        this.imageDesc = imageDesc;
    }
}
