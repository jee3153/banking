package com.example.banking.service;

import com.example.banking.entities.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    public abstract List<Account> all();
    public abstract Account accountById(long id);
    public abstract Account save(Account account);
    public abstract void delete(long id);
    public abstract Account addMoney(Account partialUpdate, long id);
}
