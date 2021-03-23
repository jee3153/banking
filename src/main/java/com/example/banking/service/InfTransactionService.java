package com.example.banking.service;

import com.example.banking.entities.Transaction;

import java.util.List;
import java.util.UUID;

public interface InfTransactionService {
    public abstract Transaction makeTransaction(Transaction transaction,long id);
    public abstract Transaction makeTransfer(Transaction transaction, long senderId, long receiverId);
    public abstract List<Transaction> getAllTransaction();
    public abstract List<Transaction> getTransactions(long accountId);
    public abstract void deleteTransaction(UUID id);
}
