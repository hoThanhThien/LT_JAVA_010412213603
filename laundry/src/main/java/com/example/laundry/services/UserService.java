package com.example.laundry.services;

import com.example.laundry.models.user.User;
import java.util.List;

public interface UserService {
    User findUserById(Long id);
    List<User> findAllUsers();
    User save(User user);
    void deleteById(Long id);
}
