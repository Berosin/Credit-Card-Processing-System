package com.example.creditcard.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne
    private User user;

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    private Double creditLimit;
    private Double availableBalance;
}