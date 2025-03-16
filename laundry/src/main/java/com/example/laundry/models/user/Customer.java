package com.example.laundry.models.user;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.Payment;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderHistory =  new ArrayList<>();

    private String preferredNotificationChannel;
    private boolean emailVerified;

    public Customer(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.Customer);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Customer");
    }

//    public Order bookOrder(LaundryShop shop, Service service, String instructions) {
//        Order order = new Order();
//        order.setCustomer(this);
//        order.setLaundryShop(shop);
//        order.setService(service);
//        order.setSpecialInstructions(instructions);
//        orderHistory.add(order);
//        return order;
//    }
//
//    public void trackOrder(Order order) {
//
//    }
//
//    public Payment makePayment(Order order, String paymentMethod, double amount) {
//        Payment payment = new Payment();
//        payment.setOrder(order);
//        payment.setPaymentMethod(paymentMethod);
//        payment.setAmount(amount);
//        return payment;
//    }
//
//    public List<LaundryShop> searchShops(LaundryShop shop) {
//        return null;
//    }
//
//    public void requestEmailVerification() {
//
//    }
//
//    public boolean emailVerified(String verificationToken) {
//        return true;
//    }
}
