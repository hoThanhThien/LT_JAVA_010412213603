<<<<<<< HEAD
package com.example.laundry.models.user;

import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="store_owners")
@DiscriminatorValue("STORE_OWNER")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StoreOwner extends User {
    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public StoreOwner(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.StoreOwner);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Store Owner");
    }
}
=======
package com.example.laundry.models.user;

import com.example.laundry.models.shop.LaundryShop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="store_owners")
@DiscriminatorValue("STORE_OWNER")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StoreOwner extends User {
    @OneToOne
    private LaundryShop shop;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public StoreOwner(String username, String password, String email, String phone, String address, Roles roles, LaundryShop shop) {
        super(username, password, email, phone, address, Roles.StoreOwner);
        this.shop = shop;
    }

    public StoreOwner(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.StoreOwner);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Store Owner");
    }
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
