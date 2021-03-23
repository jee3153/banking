package com.example.banking.controllers;

import com.example.banking.entities.Transaction;
import com.example.banking.service.InfTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;



@RestController
@RequestMapping(path = "/transaction", produces = "application/json")
public class TransactionController {

    private ConcurrentHashMap<Long, ReentrantReadWriteLock> map;
    private InfTransactionService transactionService;

    @Autowired
    public TransactionController(InfTransactionService service) {
        this.transactionService = service;
    }

    @PostMapping("/{accountId}/save")
    ResponseEntity<Transaction> makeTransaction(@RequestBody Transaction transaction, @PathVariable long accountId) {
        transactionService.makeTransaction(transaction, accountId);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/{senderId}/to/{receiverId}")
    ResponseEntity<Transaction> makeTransfer(@RequestBody Transaction transaction, @PathVariable long senderId, @PathVariable long receiverId) {
        transactionService.makeTransfer(transaction, senderId, receiverId);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    List<Transaction> getAllTransaction() {
        return transactionService.getAllTransaction();
    }

    @GetMapping("/{accountId}")
    List<Transaction> getTransactions(@PathVariable long accountId) {
        return transactionService.getTransactions(accountId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }



}
