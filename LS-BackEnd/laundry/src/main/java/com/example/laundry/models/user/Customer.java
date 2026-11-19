package com.example.laundry.models.user;

import com.example.laundry.models.order.Order;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, exclude = "orderHistory")
@ToString(exclude = "orderHistory")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderHistory = new ArrayList<>();

    public Customer(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.Customer);
    }

    public Customer(String username, String email, String phone, String address, Roles roles) {
        super(username, email, phone, address, String.valueOf(Roles.Customer));
    }

    @Override
    public void displayRole() {
        log.debug("Role: Customer");
    }
}
