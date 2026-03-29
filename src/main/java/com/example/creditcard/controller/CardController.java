package com.example.creditcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.creditcard.entity.Card;
import com.example.creditcard.service.CardService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addCard(@PathVariable Long userId, @RequestBody Card card) {
        try {
            return ResponseEntity.ok(cardService.addCard(userId, card));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCards(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cardService.getCardsByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/topup")
    public ResponseEntity<?> topUp(@RequestBody Map<String, Object> body) {
        try {
            System.out.println("=== /api/cards/topup called ===");
            System.out.println("Body received: " + body.toString());

            if (body.get("cardNumber") == null || body.get("amount") == null) {
                return ResponseEntity.badRequest().body("cardNumber and amount are required");
            }

            String cardNumber = body.get("cardNumber").toString().replaceAll("[^0-9]", "").trim();
            Double amount = Double.parseDouble(body.get("amount").toString());

            System.out.println("Parsed cardNumber=[" + cardNumber + "] amount=" + amount);

            Card updated = cardService.topUpBalance(cardNumber, amount);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            System.out.println("TOP-UP ERROR: " + e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("TOP-UP UNEXPECTED ERROR: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }
}