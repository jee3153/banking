package com.example.banking.entities;

import com.example.banking.entities.Account;
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
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Transaction {

    @Id
    @GeneratedValue
    private UUID transactionId;

    private BigDecimal payment;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
    private Timestamp paymentMadeAt;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;


}
