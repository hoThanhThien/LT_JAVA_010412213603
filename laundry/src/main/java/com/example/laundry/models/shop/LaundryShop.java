package com.example.laundry.models.shop;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="laudry_shops")
@DiscriminatorValue("LAUNDRYSHOP")
public class LaundryShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
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

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Service> services = new ArrayList<>();

    public LaundryShop(String name, String address, String openingHours) {
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
    }
}
