package com.example.banking.entities;



import com.example.banking.utils.AccountName;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name="accounts")
@Getter @Setter @NoArgsConstructor @ToString
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private AccountName accountName = AccountName.STANDARD_ACCOUNT;

    // ensure mappedBy property matches variable name in targeting class.
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

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

}
