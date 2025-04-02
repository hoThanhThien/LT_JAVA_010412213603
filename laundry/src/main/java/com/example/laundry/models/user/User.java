package com.example.laundry.models.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="user_type")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    @Column(name="user_id")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 50)
    private String email;

    @Column(unique = true, length = 11)
    private String phone;

    @Column(length = 50)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column
    private Roles roles;

    public User(String username, String password, String email, String phone, String address, Roles roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.roles = roles;
    }

    public User(String username, String password, String email, String phone, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public abstract void displayRole();
}
