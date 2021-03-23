package com.example.banking.controllers;

import com.example.banking.entities.Account;
import com.example.banking.entities.SubAccount;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.SubAccountRepository;
import com.example.banking.service.InfSubaccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(path = "/subaccount", produces = "application/json")
public class SubAccountController {

    private final InfSubaccountService subaccountService;

    @Autowired
    public SubAccountController(InfSubaccountService service) {
        this.subaccountService = service;
    }


    @GetMapping("{accountId}/all")
    List<SubAccount> getAllAccounts(@PathVariable long accountId) {
        return subaccountService.getAllAccounts(accountId);
    }

    @PostMapping("{accountId}/save")
    Account createNewSubAc(@PathVariable long accountId, @RequestBody SubAccount accountToCreate) {
       return subaccountService.createNewSubAc(accountId, accountToCreate);
    }
}
