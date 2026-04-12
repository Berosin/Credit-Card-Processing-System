package com.example.creditcard.strategy;

import com.example.creditcard.entity.Card;
import com.example.creditcard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceCheckStrategy implements PaymentStrategy {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public boolean validate(String cardNumber, Double amount) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        return card != null && card.getAvailableBalance() >= amount;
    }

    @Override
    public String getStrategyName() {
        return "Balance Check Strategy";
    }
}