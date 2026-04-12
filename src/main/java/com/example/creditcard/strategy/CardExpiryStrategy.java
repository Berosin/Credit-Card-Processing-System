package com.example.creditcard.strategy;

import com.example.creditcard.entity.Card;
import com.example.creditcard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardExpiryStrategy implements PaymentStrategy {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public boolean validate(String cardNumber, Double amount) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) return false;
        // Basic expiry check — assumes format MM/YY
        try {
            String[] parts = card.getExpiryDate().split("/");
            int month = Integer.parseInt(parts[0]);
            int year  = Integer.parseInt(parts[1]) + 2000;
            java.time.YearMonth expiry = java.time.YearMonth.of(year, month);
            return expiry.isAfter(java.time.YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getStrategyName() {
        return "Card Expiry Strategy";
    }
}