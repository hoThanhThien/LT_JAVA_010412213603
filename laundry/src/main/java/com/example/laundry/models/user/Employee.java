package com.example.laundry.models.user;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="employees")
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private LaundryShop shop;

    @ManyToOne
    @JoinColumn(name = "store_owner_id")
    private StoreOwner storeOwner;

    public Employee(String username, String password, String email, String phone, String address, LaundryShop shop) {
        super(username, password, email, phone, address);
        this.shop = shop;
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Employee at " + shop.getName());
    }

//    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
//
//    }
//
//    public void notifyCustomer(Long orderId, String message) {
//
//    }
//
//    private Order findOrderById(Long orderId) {
//        return null;
//    }
//
//    private String getCustomerUsername(Long orderId) {
//        Order order = findOrderById(orderId);
//        return order != null ? order.getCustomerUsername() : null;
//    }
}
