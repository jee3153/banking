package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.entities.SubAccount;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.SubAccountRepository;
import com.example.banking.service.SubaccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SubaccountServiceImpl implements SubaccountService {
    private final SubAccountRepository subAccountRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public SubaccountServiceImpl(SubAccountRepository subAccountRepository, AccountRepository accountRepository) {
        this.subAccountRepository = subAccountRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<SubAccount> getAllAccounts(long accountId) {
        Account superAccount = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account doesn't exist"));
        return subAccountRepository.findBySuperAccountId(accountId);
    }

    @Override
    public Account createNewSubAc(long accountId, SubAccount accountToCreate) {
        Account superAccount = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "the Account doesn't exist"));

        accountToCreate.setBalance(BigDecimal.ZERO);
        subAccountRepository.save(accountToCreate);
        superAccount.createSubAccount(accountToCreate);

        return accountRepository.save(superAccount);
    }
}
