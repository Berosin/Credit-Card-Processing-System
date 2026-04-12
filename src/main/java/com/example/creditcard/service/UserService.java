// src/main/java/com/example/creditcard/service/UserService.java
package com.example.creditcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.creditcard.entity.User;
import com.example.creditcard.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (user == null) {
            return null;
        }
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }
        if (user.getPassword() != null) {
            user.setPassword(user.getPassword().trim());
        }
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedPassword = password.trim();
        return userRepository.findFirstByEmailIgnoreCaseAndPassword(normalizedEmail, normalizedPassword);
    }
}