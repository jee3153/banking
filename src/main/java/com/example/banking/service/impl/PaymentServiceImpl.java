package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.entities.TransactionKey;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import com.example.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public PaymentServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void makePayment(BigDecimal amount, long payee, long recipient) {
        // generate credit & debit transaction key
        UUID uuid = UUID.randomUUID();
        TransactionKey creditKey = new TransactionKey(uuid, "CREDIT");
        TransactionKey debitKey = new TransactionKey(uuid, "DEBIT");

        /* get sender's AC & receiver's AC otherwise throw an error */
        Account payeeAccount = accountRepository.getOne(payee);
        Account recipientAccount = accountRepository.getOne(recipient);

        // add debit money to recipient
        recipientAccount.addBalance(amount);
        // take credit money from payee
        payeeAccount.takeBalance(amount);

        // save changes to account repository
        accountRepository.save(payeeAccount);
        accountRepository.save(recipientAccount);

        // create debit & credit transactions
        Transaction debitTransaction = new Transaction(debitKey, amount, recipientAccount, payeeAccount);
        Transaction creditTransaction = new Transaction(creditKey, amount, payeeAccount, recipientAccount);
        // post debit & credit transactions
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

    }
}
