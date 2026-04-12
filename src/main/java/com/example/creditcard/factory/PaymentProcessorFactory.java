package com.example.creditcard.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaymentProcessorFactory {

    @Autowired
    private List<PaymentProcessor> processors;

    public PaymentProcessor getProcessor(String type) {
        return processors.stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No processor found for type: " + type));
    }
}