package com.example.laundry.services.impl;

import com.example.laundry.repository.UserRepository;
import com.example.laundry.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    com.example.laundry.models.user.User user = SecurityUtils.parseUuid(identifier)
            .flatMap(userRepository::findById)
            .orElse(null);

    if (user == null) {
      user = userRepository.findByPhone(identifier);
    }
    if (user == null) {
      user = userRepository.findByUsername(identifier);
    }
    if (user == null) {
      throw new UsernameNotFoundException("User not found: " + identifier);
    }
    // Use immutable UUID as principal name so duplicate/mutable display names never collide
    return new User(user.getId().toString(), user.getPassword(), new ArrayList<>());
  }
}
