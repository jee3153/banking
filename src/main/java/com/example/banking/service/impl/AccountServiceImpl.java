package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.SubAccountRepository;
import com.example.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final SubAccountRepository subAccountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, SubAccountRepository subAccountRepository) {
        this.accountRepository = accountRepository;
        this.subAccountRepository = subAccountRepository;
    }

    @Override
    public List<Account> all() {
        return accountRepository.findAll();
    }

    @Override
    public Account accountById(long id) {
        return accountRepository.getOne(id);
    }

    @Override
    public Account save(Account account) {
//        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    @Override
    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account addMoney(Account partialUpdate, long id) {
        Account account = accountRepository.getOne(id);
        BigDecimal amount = account.getBalance();
        BigDecimal addedAmount = partialUpdate.getBalance();
        account.topUp(addedAmount);

        return accountRepository.save(account);
    }
}
