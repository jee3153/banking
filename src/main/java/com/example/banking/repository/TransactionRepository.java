package com.example.banking.repository;

import com.example.banking.entities.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
List<Transaction> findByAccount_id(Long accountId);
}
