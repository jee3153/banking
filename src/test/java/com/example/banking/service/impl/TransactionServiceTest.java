package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.repository.AccountRepository;

import com.example.banking.repository.TransactionRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;


import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;



import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
//@Transactional // rolls back after executing things to test
@Slf4j
class TransactionServiceTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeTransaction() throws Exception {

        Account ac = accountRepository.save(new Account("test account"));
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, ac, ac);

        String jsonTransaction = mvc.perform(post("/transaction/{accountId}/save", ac.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Transaction transaction = new ObjectMapper().readValue(jsonTransaction, Transaction.class);

        String jsonGetTran = mvc.perform(get("/transaction/{accountId}",  ac.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Transaction tranactionFromDatabase = transactionRepository.getOne(transaction.getTransactionId());
        Assertions.assertEquals(transaction.getTransactionId(), tranactionFromDatabase.getTransactionId());
        Assertions.assertEquals(transaction.getPayment().setScale(2, RoundingMode.CEILING), tranactionFromDatabase.getPayment());
//        Assertions.assertEquals(transaction.getAccount(), tranactionFromDatabase.getAccount());
        Assertions.assertEquals(transaction.getPaymentMadeAt(), tranactionFromDatabase.getPaymentMadeAt());



    }

    @Test
    void makeTransfer() throws Exception {

        Account senderAc = accountRepository.save(new Account("test account"));
        Account receiverAc = accountRepository.save(new Account("test account"));
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, senderAc, senderAc);

        String jsonTransaction = mvc.perform(post("/transaction/{senderId}/to/{receiverId}", senderAc.getId(), receiverAc.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Transaction transaction = new ObjectMapper().readValue(jsonTransaction, Transaction.class);

        String jsonGetTran = mvc.perform(get("/transaction/{accountId}",  senderAc.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Transaction tranMade = transactionRepository.getOne(tran.getTransactionId());

        Assertions.assertEquals(tran, tranMade);
        Assertions.assertEquals(senderAc, tran.getAccount());
    }

    @Test
    void getAllTransaction() throws Exception {

        Account ac = accountRepository.save(new Account("test account"));
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, ac, ac);



        log.info("******** START : MOC MVC test **********");
        String jsonTransaction = mvc.perform(post("/transaction/{accountId}/save", ac.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        log.info("******** START : MOC MVC test **********");

        log.info("******** START : MOC MVC test **********");
        String getTransaction = mvc.perform(get("/transaction/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("******** END : MOC MVC test **********");

        Assertions.assertNotNull(getTransaction);

    }

    @Test
    void getTransactions() throws Exception {

        Account ac = accountRepository.save(new Account("test account"));
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, ac, ac);
//        List<Transaction> transactionsOfAc = Arrays.asList(tran);

        log.info("******** START : MOC MVC test **********");
        String jsonTransaction = mvc.perform(post("/transaction/{accountId}/save", ac.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        log.info("******** START : MOC MVC test **********");
        mvc.perform(get("/transaction/{accountId}",  ac.getId())
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.info("******** END : MOC MVC test **********");


    }

    @Test
    void deleteTransaction() throws Exception {

        Account ac = accountRepository.save(new Account("test account"));
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, ac, ac);

        log.info("******** START : MOC MVC test **********");
        String jsonTransaction = mvc.perform(post("/transaction/{accountId}/save", ac.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Transaction transaction = new ObjectMapper().readValue(jsonTransaction, Transaction.class);

        String responseJson = mvc.perform(delete("/transaction/{id}", transaction.getTransactionId()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        log.info("******** END : MOC MVC test **********");


        assertEquals("Deleted Successfully", responseJson);

    }
}