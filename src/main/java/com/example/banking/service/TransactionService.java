package com.example.banking.service;

import com.example.banking.entities.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
     Transaction makeTransaction(Transaction transaction,long id);
     Transaction makeTransfer(Transaction transaction, long senderId, long receiverId);
     List<Transaction> getAllTransaction();
     List<Transaction> getTransactions(long accountId);
     void deleteTransaction(UUID id);
}
