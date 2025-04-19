package com.example.laundry.models.order;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
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

    @ManyToOne
    private LaundryShop shop;

    @ManyToOne
    private Service service;

    @CreationTimestamp
    private Date createdAt;
    private Date updatedAt;
    private String instructions;

    public Order(OrderStatus orderStatus, LaundryShop shop, Customer customer, Service service, String instructions) {
        this.orderStatus = orderStatus;
        this.shop = shop;
        this.customer = customer;
        this.service = service;
        this.instructions = instructions;
        this.createdAt = new Date();
    }

    public String getCustomerUsername() {

        return "";
    }

    public void setLaundryShop(LaundryShop shop) {
    }

    public void setSpecialInstructions(String instructions) {
        this.instructions = instructions;
    }
}
