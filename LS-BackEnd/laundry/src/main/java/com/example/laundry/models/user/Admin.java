package com.example.laundry.models.user;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.Admin);
    }

    @Override
    public void displayRole() {
        log.debug("Role: ADMIN");
    }

    public void createStoreOwner(StoreOwner storeOwner) {
        log.debug("Creating new store owner");
    }
}
