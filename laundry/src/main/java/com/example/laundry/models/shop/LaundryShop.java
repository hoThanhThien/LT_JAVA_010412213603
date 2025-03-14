package com.example.laundry.models.shop;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Service> services = new ArrayList<>();

    public LaundryShop(String name, String address, String openingHours) {
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
    }
//
//    public void addService(Service service){
//        services.add(service);
//        service.setShop(this);
//    }
//
//    public void removeService(Service service){
//        services.remove(service);
//        service.setShop(null);
//    }
}
