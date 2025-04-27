package com.example.laundry.repository;

import com.example.laundry.models.user.StoreOwner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, UUID> {
    StoreOwner findByEmail(String email);
    StoreOwner findByPhone(String phone);
    StoreOwner findByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsById(@NonNull UUID id);
}
