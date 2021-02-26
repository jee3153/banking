package com.example.banking.controllers;

import com.example.banking.entities.Account;
import com.example.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/account", produces = "application/json")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // localhost:8080/account/all
    @GetMapping("/all")
    Iterable<Account> all() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    Account accountById(@PathVariable Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User id doesn't exist"));
    }

    @PostMapping("/save")
    Account save(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable Long id) {
        accountRepository.deleteById(id);
    }

    @PatchMapping("/add/{id}")
    ResponseEntity<?> addMoney(@RequestBody Account partialUpdate, @PathVariable("id") Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
        BigDecimal amount = account.getBalance();
        BigDecimal addedAmount = partialUpdate.getBalance();
        account.topUp(addedAmount);

        accountRepository.save(account);
        return ResponseEntity.ok("Money added");
    }

    @PatchMapping("/transfer/{senderId}/to/{receiverId}")
    ResponseEntity<?> transferMoney(@RequestBody Account partialUpdate, @PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        Account senderAccount = accountRepository.findById(senderId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
        Account receiverAccount = accountRepository.findById(receiverId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
        BigDecimal amountToSend = partialUpdate.getBalance();
        BigDecimal senderBalance = senderAccount.getBalance();
        BigDecimal receiverBalance = receiverAccount.getBalance();

        // reduce balance for a sender, increase balance for a receiver
        senderAccount.setBalance(senderBalance.subtract(amountToSend));
        receiverAccount.setBalance(receiverBalance.add(amountToSend));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        return ResponseEntity.ok(amountToSend + " has been transferred.");
    }
}
