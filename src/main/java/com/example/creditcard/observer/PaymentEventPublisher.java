package com.example.creditcard.observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaymentEventPublisher {

    @Autowired
    private List<PaymentObserver> observers;

    public void notifySuccess(String cardNumber, Double amount) {
        observers.forEach(o -> o.onPaymentSuccess(cardNumber, amount));
    }

    public void notifyFailure(String cardNumber, String reason) {
        observers.forEach(o -> o.onPaymentFailure(cardNumber, reason));
    }
}