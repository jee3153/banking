package com.example.banking.entities;

import com.example.banking.entities.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private UUID transactionId;

    private BigDecimal payment;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
    private Timestamp paymentMadeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account account;


    public Transaction(UUID transactionId, Account account, BigDecimal payment, Timestamp paymentMadeAt) {
        this.transactionId = transactionId;
        this.account = account;
        this.payment = payment;
        this.paymentMadeAt = paymentMadeAt;
    }

    public Transaction() {
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Timestamp getPaymentMadeAt() {
        return paymentMadeAt;
    }

    public void setPaymentMadeAt(Timestamp paymentMadeAt) {
        this.paymentMadeAt = paymentMadeAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", account=" + account +
                ", payment=" + payment +
                ", paymentMadeAt='" + paymentMadeAt + '\'' +
                '}';
    }
}
