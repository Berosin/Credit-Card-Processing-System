package com.example.creditcard.observer;

public interface PaymentObserver {
    void onPaymentSuccess(String cardNumber, Double amount);
    void onPaymentFailure(String cardNumber, String reason);
}