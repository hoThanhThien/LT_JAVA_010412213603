package com.example.laundry.models.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
import java.util.UUID;

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
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

<<<<<<< HEAD
    @Column(unique = true, length = 50, nullable = true)
=======
    @Column(unique = true, length = 50)
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
    private String email;

    @Column(unique = true, length = 11)
    private String phone;

    @Column(length = 50)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column
    private Roles roles;

<<<<<<< HEAD
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Constructor, getters, and setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
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
