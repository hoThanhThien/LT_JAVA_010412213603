package com.example.laundry.models.user;

import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
//@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    public Admin(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.Admin);
    }

    @Override
    public void displayRole() {
        System.out.println("I am a ADMIN");
    }

    public void createStoreOwner(StoreOwner storeOwner) {
        System.out.println("Creating new store owner");
    }
}
