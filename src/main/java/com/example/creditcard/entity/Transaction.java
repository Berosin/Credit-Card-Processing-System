package com.example.creditcard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    private Card card;

    private Double amount;
    private String status;
    private String description;

    private LocalDateTime timestamp;
}