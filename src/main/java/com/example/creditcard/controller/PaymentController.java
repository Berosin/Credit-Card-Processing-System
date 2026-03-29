package com.example.creditcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.creditcard.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public String process(@RequestParam String cardNumber,
                          @RequestParam Double amount) {
        return paymentService.processPayment(cardNumber, amount);
    }
}