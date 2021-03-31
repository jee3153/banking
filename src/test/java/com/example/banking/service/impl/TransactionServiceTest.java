package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.repository.AccountRepository;

import com.example.banking.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
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

//    @Autowired
//    private WebApplicationContext ctx;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
    @Autowired
    private AccountRepository accountRepository;
//
//    @Autowired
//    WebTestClient webTestClient;
//
//    @MockBean
//    private TransactionService transactionService;

//    @BeforeEach
//    public void setup() {
//        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
//                .alwaysDo(print())
//                .build();
//    }

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
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Transaction tran = new Transaction(uuid, BigDecimal.valueOf(7.77), null, ac);

       mvc.perform(post("/transaction/{accountId}/save", 1)
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(tran))
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.transactionId").value("00000000-0000-0000-0000-000000000000"))
               .andExpect(jsonPath("$.payment").value("7.77"))
               .andDo(print());

    }

    @Test
    void makeTransfer() throws Exception {

        Account senderAc = new Account(1L);
        Account receiverAc = new Account(2L);
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Transaction tran = new Transaction(uuid, BigDecimal.TEN, null, senderAc);

        mvc.perform(post("/transaction/{senderId}/to/{receiverId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("00000000-0000-0000-0000-000000000000"))
                .andExpect(jsonPath("$.payment").value("10"))
                .andDo(print());

    }

    @Test
    void getAllTransaction() throws Exception {

        Account ac = new Account(2L);
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Transaction tran = new Transaction(uuid, BigDecimal.TEN, null, ac);
        List<Transaction> allTransactions = Arrays.asList(tran);

//        given(transactionService.getAllTransaction()).willReturn(allTransactions);

        log.info("******** START : MOC MVC test **********");
        mvc.perform(get("/transaction/all")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].payment").value(tran.getPayment()))
                .andDo(print());
        log.info("******** END : MOC MVC test **********");

        log.info("******** START : TestRestTemplate test **********");
//        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("/transaction/all", Transaction[].class);
//        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        then(response.getBody()).isNotNull();
        log.info("******** END : TestRestTemplate test **********");
    }

    @Test
    void getTransactions() throws Exception {

        Account ac = new Account(2L);
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Transaction tran = new Transaction(uuid, BigDecimal.ONE, null, ac);
        List<Transaction> transactionsOfAc = Arrays.asList(tran);

//        given(transactionService.getTransactions(ac.getId())).willReturn(transactionsOfAc);

        log.info("******** START : MOC MVC test **********");
        mvc.perform(get("/transaction/{accountId}", 2)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        log.info("******** END : MOC MVC test **********");

        log.info("******** START : TestRestTemplate test **********");
//        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("/transaction/2", Transaction[].class);
//        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        then(response.getBody()).isNotNull();
        log.info("******** END : TestRestTemplate test **********");
    }

    @Test
    void deleteTransaction() throws Exception {

        Account ac = accountRepository.save(new Account("test account"));
        UUID uuid = UUID.randomUUID();
        Transaction tran = new Transaction(null, BigDecimal.TEN, null, ac);
//        given(transactionService.makeTransaction(tran, 2)).willReturn(tran);

        log.info("******** START : MOC MVC test **********");
        String jsonTransaction = mvc.perform(post("/transaction/{accountId}/save", ac.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tran))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Transaction transaction = new ObjectMapper().readValue(jsonTransaction, Transaction.class);

        String responseJson = mvc.perform(delete("/transaction/{id}", transaction.getTransactionId()))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        log.info("******** END : MOC MVC test **********");


        assertEquals("Deleted Successfully", responseJson);

    }
}