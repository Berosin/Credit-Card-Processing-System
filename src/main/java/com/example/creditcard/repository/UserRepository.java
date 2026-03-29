package com.example.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.creditcard.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
}