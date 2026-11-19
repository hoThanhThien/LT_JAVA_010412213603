package com.example.laundry.models.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@Entity
@Table(name="store_owners")
@DiscriminatorValue("STORE_OWNER")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(exclude = "employees")
public class StoreOwner extends User {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public StoreOwner(String username, String password, String email, String phone, String address, Roles roles) {
        super(username, password, email, phone, address, Roles.StoreOwner);
    }

    @Override
    public void displayRole() {
        log.debug("Role: Store Owner");
    }
}
