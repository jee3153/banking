package com.example.banking.service;

import com.example.banking.entities.Account;
import com.example.banking.entities.SubAccount;

import java.util.List;

public interface SubaccountService {
    public abstract List<SubAccount> getAllAccounts(long accountId);
    public abstract Account createNewSubAc(long accountId,SubAccount accountToCreate);
}
