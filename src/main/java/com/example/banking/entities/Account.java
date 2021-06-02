package com.example.banking.entities;



import com.example.banking.utils.AccountName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name="accounts")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "transactions"})
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private AccountName accountName = AccountName.STANDARD_ACCOUNT;

    // ensure mappedBy property matches variable name in targeting class.
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    private BigDecimal balance;

    @OneToMany(mappedBy = "superAccount", cascade = CascadeType.ALL)
    private List<SubAccount> accounts = new ArrayList<>();

    public Account(Long id, String accountName) {
        this.id = id;
        this.balance = BigDecimal.ZERO;

        switch (accountName) {
            case "advance": { this.accountName = AccountName.ADVANCE_ACCOUNT; }
            case "student": { this.accountName = AccountName.STUDENT_ACCOUNT; }
            case "premier": { this.accountName = AccountName.PREMIER_ACCOUNT; }
        }
    }

    public Account(String accountName) {
        this.balance = BigDecimal.ZERO;

        switch (accountName) {
            case "advance": { this.accountName = AccountName.ADVANCE_ACCOUNT; }
            case "student": { this.accountName = AccountName.STUDENT_ACCOUNT; }
            case "premier": { this.accountName = AccountName.PREMIER_ACCOUNT; }
        }
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public void topUp(BigDecimal amount) {
        BigDecimal balance = getBalance();
        setBalance(balance.add(amount));
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void transfer(Account receiver, BigDecimal amount) {

        this.takeBalance(amount);
        receiver.addBalance(amount);
    }

    public void addBalance(BigDecimal amount) {

        BigDecimal balance = getBalance();
        setBalance(balance.add(amount));
    }

    public void takeBalance(BigDecimal amount) {

        BigDecimal balance = getBalance();

        if (balance.compareTo(amount) >= 0) {
            setBalance(balance.subtract(amount));
        }
    }

    public void createSubAccount(SubAccount account) {

        this.accounts.add(account);
        account.setSuperAccount(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountName getAccountName() {
        return accountName;
    }

    public void setAccountName(AccountName accountName) {
        this.accountName = accountName;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<SubAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SubAccount> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountName=" + accountName +
                ", transactions=" + transactions +
                ", balance=" + balance +
                ", accounts=" + accounts +
                '}';
    }
}
