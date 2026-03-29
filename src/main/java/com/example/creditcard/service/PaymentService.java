package com.example.creditcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.creditcard.entity.*;
import com.example.creditcard.repository.*;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public String processPayment(String cardNumber, Double amount) {

        Card card = cardRepository.findByCardNumber(cardNumber);

        if (card == null) return "Invalid Card";

        if (card.getAvailableBalance() < amount) {
            return "Insufficient Balance";
        }

        card.setAvailableBalance(card.getAvailableBalance() - amount);
        cardRepository.save(card);

        Transaction txn = new Transaction();
        txn.setCard(card);
        txn.setAmount(amount);
        txn.setStatus("SUCCESS");
        txn.setDescription("Payment Done");
        txn.setTimestamp(LocalDateTime.now());

        transactionRepository.save(txn);

        return "Payment Successful";
    }
}