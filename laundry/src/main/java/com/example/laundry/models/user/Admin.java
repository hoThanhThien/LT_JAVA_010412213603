package com.example.laundry.models.user;

import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @Override
    public void displayRole() {
        System.out.println("I am a ADMIN");
    }

    public void createStoreOwner(StoreOwner storeOwner) {
        System.out.println("Creating new store owner: " + storeOwner.getUsername());
    }

    public void verifyShopDetails(LaundryShop shop) {
        System.out.println("Verifying shop: " + shop.getName());
    }

    public void manageUsers(User user) {
        System.out.println("Managing user: " + user.getUsername());
    }

    public void sendPlatformNotification(String message) {

    }

    public void generatePlatformReport() {

    }
}
