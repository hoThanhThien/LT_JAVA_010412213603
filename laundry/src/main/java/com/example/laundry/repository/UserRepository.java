package com.example.laundry.repository;

import com.example.laundry.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(long id);
    List<User> findAllUsers();
    void deleteById(long id);
}
