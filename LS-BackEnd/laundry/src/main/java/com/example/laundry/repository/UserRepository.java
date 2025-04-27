package com.example.laundry.repository;

import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByPhone(String phone);
//    User findUserById(Long id);
//    List<User> findAllUsers();
//    void deleteById(Long id);
}
