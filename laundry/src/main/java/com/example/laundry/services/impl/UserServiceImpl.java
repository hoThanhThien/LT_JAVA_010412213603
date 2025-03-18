package com.example.laundry.services.impl;

import com.example.laundry.models.user.User;
import com.example.laundry.repository.UserRepository;
import com.example.laundry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

//    @Override
//    public User findUserById(Long id) {
//        return userRepository.findUserById(id);
//    }
//
//    @Override
//    public List<User> findAllUsers() {
//        return userRepository.findAllUsers();
//    }
//
//    @Override
//    public User save(User user) {
//        return userRepository.save(user);
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        userRepository.deleteById(id);
//    }
}
