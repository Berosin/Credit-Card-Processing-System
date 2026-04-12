package com.example.creditcard.service;

import com.example.creditcard.entity.*;
import com.example.creditcard.repository.*;
import com.example.creditcard.factory.*;
import com.example.creditcard.observer.PaymentEventPublisher;
import com.example.creditcard.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    // ── existing repositories (kept as is) ──
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // ── Strategy Pattern ──
    @Autowired
    private List<PaymentStrategy> strategies;

    // ── Factory Pattern ──
    @Autowired
    private PaymentProcessorFactory processorFactory;

    // ── Observer Pattern ──
    @Autowired
    private PaymentEventPublisher eventPublisher;

    public String processPayment(String cardNumber, Double amount) {

        // STEP 1: Check card exists first (same as your original)
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) {
            eventPublisher.notifyFailure(cardNumber, "Invalid Card");
            return "Invalid Card";
        }

        // STEP 2: Strategy Pattern — run all validation strategies
        System.out.println("\n=== Strategy Pattern ===");
        for (PaymentStrategy strategy : strategies) {
            boolean valid = strategy.validate(cardNumber, amount);
            System.out.println(strategy.getStrategyName() + " → " + (valid ? "PASSED" : "FAILED"));
            if (!valid) {
                String reason = strategy.getStrategyName() + " failed";
                eventPublisher.notifyFailure(cardNumber, reason);
                if (strategy instanceof BalanceCheckStrategy) return "Insufficient Balance";
                if (strategy instanceof CardExpiryStrategy)  return "Card Expired";
                return "Validation Failed";
            }
        }

        // STEP 3: Factory Pattern — get the right processor
        System.out.println("\n=== Factory Pattern ===");
        PaymentProcessor processor = processorFactory.getProcessor("CREDIT");
        System.out.println("Processor selected: " + processor.getType());

        // STEP 4: Process the payment
        String result = processor.process(cardNumber, amount);

        // STEP 5: Observer Pattern — notify result
        System.out.println("\n=== Observer Pattern ===");
        if ("Payment Successful".equals(result)) {
            eventPublisher.notifySuccess(cardNumber, amount);
        } else {
            eventPublisher.notifyFailure(cardNumber, result);
        }

        return result;
    }
}