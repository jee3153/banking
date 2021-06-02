package com.example.banking.entities;

import com.example.banking.entities.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name="Transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Transaction {

    @EmbeddedId
//    @GeneratedValue
    private TransactionKey transactionKey;

    private BigDecimal payment;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss.SSS a")
    private Timestamp paymentMadeAt;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incoming_id")
    private Account participant;

    public Transaction(TransactionKey transactionKey, BigDecimal payment, Account account, Account participant) {
        this.transactionKey = transactionKey;
        this.payment = payment;
        this.account = account;
        this.participant = participant;
    }

    public TransactionKey getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(TransactionKey transactionKey) {
        this.transactionKey = transactionKey;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getParticipant() {
        return participant;
    }

    public void setParticipant(Account participant) {
        this.participant = participant;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionKey=" + transactionKey +
                ", payment=" + payment +
                ", paymentMadeAt=" + paymentMadeAt +
                ", account=" + (account != null ? account.getId() : "") +
                ", incomingId=" + (participant != null ? participant.getId() : "") +
                '}';
    }

}
