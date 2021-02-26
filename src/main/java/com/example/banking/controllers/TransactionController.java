package com.example.banking.controllers;

import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(path = "/transaction", produces = "application/json")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping("{accountId}/save")
    Transaction postTransactions(@RequestParam double amount, @PathVariable long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User id doesn't exist"
        ));

        Transaction transaction = new Transaction(null, account, BigDecimal.valueOf(amount), null);

       return transactionRepository.save(transaction);
    }

    @GetMapping("/all")
    Iterable<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{accountId}")
    Iterable<Transaction> getTransactions(@RequestBody Transaction transaction, @PathVariable long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User id doesn't exist"
        ));
        return transactionRepository.findByAccount_id(accountId);
    }

    @DeleteMapping("/{id}")
    void deleteTransaction(@PathVariable UUID id) { transactionRepository.deleteById(id); }

    // transaction: deducting money
//    @GetMapping("/save/{account_id}")
//    Transaction save(@RequestBody Transaction transaction, @PathVariable Long accountId) {
//
//    }

}
