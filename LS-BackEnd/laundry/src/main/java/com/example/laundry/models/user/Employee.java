package com.example.laundry.models.user;

import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"shop", "storeOwner"})
@ToString(exclude = {"shop", "storeOwner"})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="employees")
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private LaundryShop shop;

    @ManyToOne
    @JoinColumn(name = "store_owner_id")
    private StoreOwner storeOwner;

    public Employee(String username, String password, String email, String phone, String address, Roles roles, LaundryShop shop) {
        super(username, password, email, phone, address, Roles.Employee);
        this.shop = shop;
    }

    public Employee(String username, String encodedPassword, String email, String phone, String address, Roles roles) {
        super(username, encodedPassword, email, phone, address, Roles.Employee);
    }

    public Employee(String username, String encodedPassword, String email, String phone, String address, Roles roles, StoreOwner storeOwner) {
        super(username, encodedPassword, email, phone, address, Roles.Employee);
        this.storeOwner = storeOwner;
    }

    public Employee(String username, String encodedPassword, String email, String phone, String address, Roles roles, String avtUser, Date createdAt, Date updatedAt, StoreOwner storeOwner) {
        super(username, encodedPassword, email, phone, address, Roles.Employee);
        this.setAvtUser(avtUser);
        if (createdAt != null) this.setCreatedAt(createdAt);
        if (updatedAt != null) this.setUpdatedAt(updatedAt);
        this.storeOwner = storeOwner;
    }

  @Override
    public void displayRole() {
        System.out.println("Role: Employee at ");
    }
}
