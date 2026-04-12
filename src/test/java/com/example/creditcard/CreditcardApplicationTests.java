package com.example.creditcard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditcardApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static Long userId;
    static String cardNumber = "4111111111111111";

    @Test
    @Order(1)
    public void testRegisterUser() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Test User");
        user.put("email", "testuser@gmail.com");
        user.put("password", "1234");

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andReturn();

        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {});
        userId = Long.valueOf(map.get("userId").toString());
        System.out.println("TEST 1 PASSED: User registered with ID = " + userId);
    }

    @Test
    @Order(2)
    public void testSuccessfulLogin() throws Exception {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", "testuser@gmail.com");
        creds.put("password", "1234");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));

        System.out.println("TEST 2 PASSED: Login successful with valid credentials");
    }

    @Test
    @Order(3)
    public void testSuccessfulLoginWithUppercaseEmailAndSpaces() throws Exception {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", "  TESTUSER@GMAIL.COM  ");
        creds.put("password", "1234");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));

        System.out.println("TEST 3 PASSED: Login successful with uppercase email and spaces");
    }

    @Test
    @Order(4)
    public void testFailedLogin() throws Exception {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", "wrong@gmail.com");
        creds.put("password", "wrongpass");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creds)))
            .andExpect(status().isUnauthorized());

        System.out.println("TEST 4 PASSED: Invalid login handled correctly");
    }

    @Test
    @Order(5)
    public void testAddCard() throws Exception {
        Map<String, Object> card = new HashMap<>();
        card.put("cardNumber", cardNumber);
        card.put("expiryDate", "12/28");
        card.put("cvv", "123");
        card.put("creditLimit", 50000);
        card.put("availableBalance", 50000);

        mockMvc.perform(post("/api/cards/add/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(cardNumber));

        System.out.println("TEST 4 PASSED: Card added for user " + userId);
    }

    @Test
    @Order(6)
    public void testGetCardsByUser() throws Exception {
        mockMvc.perform(get("/api/cards/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        System.out.println("TEST 5 PASSED: Cards retrieved for user " + userId);
    }

    @Test
    @Order(7)
    public void testSuccessfulPayment() throws Exception {
        mockMvc.perform(post("/api/payments/process")
                .param("cardNumber", cardNumber)
                .param("amount", "1000"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment Successful"));

        System.out.println("TEST 6 PASSED: Payment of Rs.1000 processed successfully");
    }

    @Test
    @Order(8)
    public void testInsufficientBalance() throws Exception {
        mockMvc.perform(post("/api/payments/process")
                .param("cardNumber", cardNumber)
                .param("amount", "9999999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Insufficient Balance"));

        System.out.println("TEST 7 PASSED: Insufficient balance detected correctly");
    }

    @Test
    @Order(9)
    public void testInvalidCard() throws Exception {
        mockMvc.perform(post("/api/payments/process")
                .param("cardNumber", "0000000000000000")
                .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid Card"));

        System.out.println("TEST 8 PASSED: Invalid card detected correctly");
    }

    @Test
    @Order(10)
    public void testTopUpBalance() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("cardNumber", cardNumber);
        body.put("amount", 10000);

        mockMvc.perform(put("/api/cards/topup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(cardNumber));

        System.out.println("TEST 9 PASSED: Balance topped up successfully");
    }
}