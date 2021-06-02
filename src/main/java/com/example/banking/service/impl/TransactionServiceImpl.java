package com.example.banking.service.impl;

import com.example.banking.entities.Account;
import com.example.banking.entities.Transaction;
import com.example.banking.entities.TransactionKey;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import com.example.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private ConcurrentHashMap<Long, ReentrantReadWriteLock> map = new ConcurrentHashMap<>();

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    private <T> T withReadLock(long accountId,  Supplier<T> function) {
        ReentrantReadWriteLock lock = map.computeIfAbsent(accountId, (id) -> new ReentrantReadWriteLock(true));
        try {
            lock.readLock().lock();
            return function.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    private <T> T withWriteLock(long accountId, Supplier<T> function) {
        ReentrantReadWriteLock lock = map.computeIfAbsent(accountId, (id) -> new ReentrantReadWriteLock(true));
        try {
            lock.writeLock().lock();
            return function.get();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Transaction makeTransfer(Transaction debitTransaction, long senderId, long receiverId) {

        ReentrantReadWriteLock lock = map.computeIfAbsent(senderId, (id) -> new ReentrantReadWriteLock(true));

        try {

            lock.writeLock().lock();
            UUID uuid = UUID.randomUUID();
            TransactionKey credit = new TransactionKey(uuid, "CREDIT");
            TransactionKey debit = new TransactionKey(uuid, "DEBIT");

            /* get sender's AC & receiver's AC otherwise throw an error */
            Account senderAccount = accountRepository.getOne(senderId);
            Account receiverAccount = accountRepository.getOne(receiverId);



            BigDecimal amount = debitTransaction.getPayment(); // get amount of money from transaction
            Transaction creditTransaction = new Transaction(credit, amount, senderAccount, senderAccount);

            debitTransaction.setTransactionKey(debit);
            debitTransaction.setParticipant(receiverAccount);
            senderAccount.transfer(receiverAccount, amount); // transfer money from a sender to a receiver
            receiverAccount.addTransaction(creditTransaction);
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            return transactionRepository.saveAll(List.of(debitTransaction, creditTransaction)).get(0);

        } finally {

            lock.writeLock().unlock();
        }

    }


    @Override
    public Transaction makeTransaction(Transaction transaction, long accountId) {
        return withWriteLock(accountId, () -> {
            Account account = accountRepository.getOne(accountId);

            BigDecimal amount = transaction.getPayment();

            TransactionKey transactionKey = new TransactionKey(UUID.randomUUID(), "DEBIT");
            transaction.setTransactionKey(transactionKey);

            account.takeBalance(amount);
            account.addTransaction(transaction);
            transaction.setAccount(account);
            accountRepository.save(account);

//            Transaction transaction = new Transaction(null, account, BigDecimal.valueOf(amount), null);

            return transactionRepository.save(transaction);
        });
    }


    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactions(long accountId) {
        return withReadLock(accountId, () -> {

            Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User id doesn't exist"));

            return transactionRepository.findByAccountId(accountId);

        });
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }
}
