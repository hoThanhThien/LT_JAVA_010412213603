package com.example.laundry.models.shop;

import com.example.laundry.models.user.StoreOwner;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(exclude = {"serviceCategories", "storeOwner"})
@ToString(exclude = {"serviceCategories", "storeOwner"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="laudry_shops")
@DiscriminatorValue("LAUNDRYSHOP")
public class LaundryShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column
    private String description;

    @Column(name = "average_rating")
    private double averageRating;

    @Column(name = "created_at")
    private Date createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<ServiceCategory> serviceCategories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "store_owner_id")
    private StoreOwner storeOwner;

    public LaundryShop(Long id, String name, String address, String openingHours, String description, double averageRating, StoreOwner storeOwner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
        this.description = description;
        this.storeOwner = storeOwner;
        this.averageRating = 0.0;
        this.serviceCategories = new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
