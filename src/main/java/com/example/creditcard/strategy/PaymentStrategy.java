package com.example.creditcard.strategy;

public interface PaymentStrategy {
    boolean validate(String cardNumber, Double amount);
    String getStrategyName();
}