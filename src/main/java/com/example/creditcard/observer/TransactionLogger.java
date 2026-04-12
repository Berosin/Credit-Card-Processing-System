package com.example.creditcard.observer;

import org.springframework.stereotype.Component;

@Component
public class TransactionLogger implements PaymentObserver {

    @Override
    public void onPaymentSuccess(String cardNumber, Double amount) {
        System.out.println("[LOG] Payment SUCCESS — Card: ****"
            + cardNumber.substring(cardNumber.length() - 4)
            + " | Amount: ₹" + amount);
    }

    @Override
    public void onPaymentFailure(String cardNumber, String reason) {
        System.out.println("[LOG] Payment FAILED — Card: ****"
            + cardNumber.substring(Math.max(0, cardNumber.length() - 4))
            + " | Reason: " + reason);
    }
}