package com.example.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.creditcard.entity.Card;
import com.example.creditcard.entity.User;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);
    List<Card> findByUser(User user);
    boolean existsByCardNumber(String cardNumber);

    @Query(value = "SELECT * FROM card WHERE TRIM(card_number) = TRIM(:cardNumber) LIMIT 1", nativeQuery = true)
    Card findByCardNumberNative(@Param("cardNumber") String cardNumber);

    @Query(value = "SELECT * FROM card WHERE REGEXP_REPLACE(card_number, '[^0-9]', '') = :cardNumber LIMIT 1", nativeQuery = true)
    Card findByCardNumberNormalized(@Param("cardNumber") String cardNumber);
}