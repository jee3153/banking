package com.example.banking.repository;

import com.example.banking.entities.SubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubAccountRepository extends JpaRepository<SubAccount, Long> {
    List<SubAccount> findBySuperAccountId(Long accountId);
}
