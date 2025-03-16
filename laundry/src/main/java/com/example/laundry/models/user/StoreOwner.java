package com.example.laundry.models.user;

import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.report.Report;
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
//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="store_owners")
@DiscriminatorValue("STORE_OWNER")
public class StoreOwner extends User {
    @OneToOne
    private LaundryShop shop;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public StoreOwner(String username, String password, String email, String phone, String address, Roles roles, LaundryShop shop) {
        super(username, password, email, phone, address, Roles.StoreOwner);
        this.shop = shop;
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Store Owner of " + shop.getName());
    }
//
//    public void notifyCustomer(Long orderId, String message){
//
//    }
//
//    public void addService(Service service) {
//
//    }
//
//    public void addEmployee(Employee employee) {
//
//    }
//
//    public Report generateFinacialReport() {
//        return new Report();
//    }
}
