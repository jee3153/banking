package com.example.banking.controllers;

import com.example.banking.entities.Account;
import com.example.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account", produces = "application/json")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService service) {
        this.accountService = service;
    }

    // localhost:8080/account/all
    @GetMapping("/all")
    List<Account> all() {
        return accountService.all();
    }

    @GetMapping("/{id}")
    Account accountById(@PathVariable long id) {
        return accountService.accountById(id);
    }

    @PostMapping("/save")
    Account save(@RequestBody Account account) {
        return accountService.save(account);
    }

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable long id) {
        accountService.delete(id);
    }

    @PatchMapping("/add/{id}")
    ResponseEntity<Account> addMoney(@RequestBody Account partialUpdate, @PathVariable("id") long id) {
        Account account = accountService.addMoney(partialUpdate, id);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

//    @PatchMapping("/transfer/{senderId}/to/{receiverId}")
//    ResponseEntity<?> transferMoney(@RequestBody Account partialUpdate, @PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
//        Account senderAccount = accountRepository.findById(senderId).orElseThrow(() -> new ResponseStatusException(
//                HttpStatus.NOT_FOUND));
//        Account receiverAccount = accountRepository.findById(receiverId).orElseThrow(() -> new ResponseStatusException(
//                HttpStatus.NOT_FOUND));
//
//        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//
//        BigDecimal amountToSend = partialUpdate.getBalance();
//        BigDecimal senderBalance = senderAccount.getBalance();
//        BigDecimal receiverBalance = receiverAccount.getBalance();
//
//        // reduce balance for a sender, increase balance for a receiver
//        senderAccount.setBalance(senderBalance.subtract(amountToSend));
//        receiverAccount.setBalance(receiverBalance.add(amountToSend));
//
//        accountRepository.save(senderAccount);
//        accountRepository.save(receiverAccount);
//
//        return ResponseEntity.ok(amountToSend + " has been transferred.");
//    }
}
