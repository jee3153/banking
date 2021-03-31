package com.example.banking.controllers;

import com.example.banking.entities.Account;
import com.example.banking.entities.SubAccount;
import com.example.banking.service.SubaccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/subaccount", produces = "application/json")
public class SubAccountController {

    private final SubaccountService subaccountService;

    @Autowired
    public SubAccountController(SubaccountService service) {
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
