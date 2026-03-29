package com.example.creditcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.creditcard.entity.*;
import com.example.creditcard.repository.*;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private String normalizeCardNumber(String cardNumber) {
        if (cardNumber == null) {
            throw new RuntimeException("cardNumber is required");
        }
        return cardNumber.replaceAll("[^0-9]", "").trim();
    }

    private double zeroSafe(Double value) {
        return value == null ? 0.0 : value;
    }

    public Card addCard(Long userId, Card card) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String normalizedNumber = normalizeCardNumber(card.getCardNumber());
        card.setCardNumber(normalizedNumber);

        Card existingCard = cardRepository.findByCardNumber(normalizedNumber);
        if (existingCard != null) {
            double incomingCredit = zeroSafe(card.getCreditLimit());
            double incomingAvailable = zeroSafe(card.getAvailableBalance());
            existingCard.setCreditLimit(zeroSafe(existingCard.getCreditLimit()) + incomingCredit);
            existingCard.setAvailableBalance(zeroSafe(existingCard.getAvailableBalance()) + incomingAvailable + incomingCredit);
            existingCard.setUser(user);
            return cardRepository.save(existingCard);
        }

        card.setUser(user);
        if (card.getAvailableBalance() == null) {
            card.setAvailableBalance(0.0);
        }
        if (card.getCreditLimit() == null) {
            card.setCreditLimit(0.0);
        }

        return cardRepository.save(card);
    }

    public List<Card> getCardsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return cardRepository.findByUser(user);
    }

    public Card topUpBalance(String cardNumber, Double amount) {
        String clean = normalizeCardNumber(cardNumber);

        System.out.println("=== TOP-UP DEBUG ===");
        System.out.println("Received  : [" + cardNumber + "]");
        System.out.println("Cleaned   : [" + clean + "] length=" + clean.length());

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Card card = cardRepository.findByCardNumber(clean);
        System.out.println("Exact match found: " + (card != null));

        if (card == null) {
            card = cardRepository.findByCardNumberNative(clean);
            System.out.println("Native TRIM match found: " + (card != null));
        }

        if (card == null) {
            card = cardRepository.findByCardNumberNormalized(clean);
            System.out.println("Normalized DB regexp match found: " + (card != null));
        }

        if (card == null) {
            System.out.println("Scanning all cards in DB...");
            List<Card> all = cardRepository.findAll();
            for (Card c : all) {
                String dbCard = normalizeCardNumber(c.getCardNumber());
                System.out.println("  DB card: [" + c.getCardNumber() + "] cleaned=[" + dbCard + "]");
                if (dbCard.equals(clean)) {
                    card = c;
                    System.out.println("  MATCHED via manual scan!");
                    break;
                }
            }
        }

        if (card == null) {
            System.out.println("CARD NOT FOUND for: [" + clean + "]");
            throw new RuntimeException("Card not found: " + clean);
        }

        card.setAvailableBalance(zeroSafe(card.getAvailableBalance()) + amount);
        card.setCreditLimit(zeroSafe(card.getCreditLimit()) + amount);
        Card saved = cardRepository.save(card);
        System.out.println("Top-up success! New balance: " + saved.getAvailableBalance());
        return saved;
    }
}