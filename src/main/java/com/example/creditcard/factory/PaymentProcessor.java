package com.example.creditcard.factory;

public interface PaymentProcessor {
    String process(String cardNumber, Double amount);
    String getType();
}