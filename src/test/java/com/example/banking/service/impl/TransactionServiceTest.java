package com.example.banking.service.impl;

import com.example.banking.controllers.TransactionController;
import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import org.hamcrest.core.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService service;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Transaction transaction;
    @Mock
    private Account account;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeTransaction() throws Exception {
        Account ac = new Account(1L);
        UUID uuid = UUID.randomUUID();
        Transaction tran = new Transaction(uuid, BigDecimal.TEN, null, ac);

       mvc.perform(post("/transaction/{accountId}/save", 1)
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(tran))
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.account.id").exists());

    }

    @Test
    void makeTransfer() throws Exception {
        Account senderAc = new Account(1L);

        UUID uuid = UUID.randomUUID();
        Transaction tran = new Transaction(uuid, BigDecimal.TEN, null, senderAc);

        mvc.perform(post("/transaction/{senderId}/to/{receiverId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.id").exists());

    }

    @Test
    void getAllTransaction() throws Exception {
        Account ac = new Account(2L);
        Transaction tran = new Transaction(UUID.randomUUID(), BigDecimal.TEN, null, ac);
        List<Transaction> allTransactions = Arrays.asList(tran);

        given(service.getAllTransaction()).willReturn(allTransactions);

        mvc.perform(get("/transaction/all")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].payment").value(tran.getPayment()));
    }

    @Test
    void getTransactions() throws Exception {
        Account ac = new Account(2L);
        Transaction tran = new Transaction(UUID.randomUUID(), BigDecimal.TEN, null, ac);
        List<Transaction> transactionsOfAc = Arrays.asList(tran);

        given(service.getTransactions(ac.getId())).willReturn(transactionsOfAc);

        mvc.perform(get("/transaction/{accountId}", 2)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].account.id").value(2));
    }

    @Test
    void deleteTransaction() throws Exception {
        mvc.perform(delete("/transaction/{id}", UUID.randomUUID()))
                .andExpect(status().isAccepted());
    }
}