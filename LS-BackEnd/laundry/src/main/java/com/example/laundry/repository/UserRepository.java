package com.example.laundry.repository;

import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByPhone(String phone);
    User findByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
//    User findUserById(Long id);
//    List<User> findAllUsers();
//    void deleteById(Long id);
}
