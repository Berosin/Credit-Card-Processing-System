package com.example.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.creditcard.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}