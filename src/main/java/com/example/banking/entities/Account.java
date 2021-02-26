package com.example.banking.entities;



import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="accounts")
public class Account {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    private BigDecimal balance;


    public Account(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void topUp(BigDecimal amount) {
        BigDecimal balance = getBalance();
        setBalance(balance.add(amount));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
