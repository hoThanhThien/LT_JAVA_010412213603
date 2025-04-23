package com.example.laundry.models.order;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double totalAmount;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "img_product")
    private  String imgProduct;

    @ManyToOne
    private Customer customer;
    private String address;
    @ManyToOne
    private LaundryShop laundryShop;


    @ManyToOne
    private ServiceCategory serviceCategory;

    @ManyToOne
    private Service service;

    @Column(name = "order_volume")
    private Double orderVolume;

    @CreationTimestamp
    private Date createdAt;
    private Date updatedAt;
    private String instructions;

    public Order(OrderStatus orderStatus,String imgProduct, LaundryShop laundryShop, Customer customer, ServiceCategory serviceCategory, Service service, Double orderVolume, String instructions) {
        this.orderStatus = orderStatus;
        this.imgProduct = imgProduct;
        this.laundryShop = laundryShop;
        this.customer = customer;
        this.serviceCategory = serviceCategory;
        this.service = service;
        this.orderVolume = orderVolume;
        this.instructions = instructions;
        this.createdAt = new Date();
    }

    public String getCustomerUsername() {

        return "";
    }

    public void setSpecialInstructions(String instructions) {
        this.instructions = instructions;
    }


}
