package com.example.creditcard.factory;

import com.example.creditcard.entity.*;
import com.example.creditcard.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class CreditCardProcessor implements PaymentProcessor {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public String process(String cardNumber, Double amount) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) return "Invalid Card";
        if (card.getAvailableBalance() < amount) return "Insufficient Balance";

        card.setAvailableBalance(card.getAvailableBalance() - amount);
        cardRepository.save(card);

        Transaction txn = new Transaction();
        txn.setCard(card);
        txn.setAmount(amount);
        txn.setStatus("SUCCESS");
        txn.setDescription("Processed by CreditCardProcessor");
        txn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(txn);

        return "Payment Successful";
    }

    @Override
    public String getType() {
        return "CREDIT";
    }
}