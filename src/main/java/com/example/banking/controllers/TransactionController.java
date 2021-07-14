package com.example.banking.controllers;

import com.example.banking.entities.Transaction;
import com.example.banking.entities.TransactionKey;
import com.example.banking.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/transaction", produces = "application/json")
@CrossOrigin
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService service) {
        this.transactionService = service;
        log.info("Started transaction controller");
    }

    @PostMapping("/{accountId}/save")
    ResponseEntity<Transaction> makeTransaction(@RequestBody Transaction transaction, @PathVariable long accountId) {
        log.info("Executed make transaction");
        transactionService.makeTransaction(transaction, accountId);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/{senderId}/to/{receiverId}")
    ResponseEntity<Transaction> makeTransfer(
        @RequestBody Transaction transaction,
        @PathVariable long senderId,
        @PathVariable long receiverId
    ) {
        log.info("Executed make transfer");
        transactionService.makeTransfer(transaction, senderId, receiverId);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    List<Transaction> getAllTransaction() {
        log.info("getAllTransaction executed");
        return transactionService.getAllTransaction();
    }

    @GetMapping("/{accountId}")
    List<Transaction> getTransactions(@PathVariable long accountId) {
        log.info("getTransactions by id executed");
        return transactionService.getTransactions(accountId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteTransaction(@PathVariable UUID id) {
        log.info("Executed deleteTransaction");
        transactionService.deleteTransaction(id);
        return new ResponseEntity<String>("Deleted Successfully", HttpStatus.NO_CONTENT);
    }


}
