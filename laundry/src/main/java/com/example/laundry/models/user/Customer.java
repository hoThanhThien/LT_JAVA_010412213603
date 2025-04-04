package com.example.laundry.models.user;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.Payment;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderHistory =  new ArrayList<>();

    public Customer(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.Customer);
    }

    public Customer(String username, String email, String phone, String address, Roles roles) {
        super(username, email, phone, address, String.valueOf(Roles.Customer));
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Customer");
    }
}
