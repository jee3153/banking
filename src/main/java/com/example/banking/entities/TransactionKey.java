package com.example.banking.entities;

import java.io.Serializable;
import java.util.UUID;


public class TransactionKey implements Serializable {
    private UUID transactionId;
    private String type;

    public TransactionKey() {
    }

    public TransactionKey(UUID transactionId, String type) {
        this.transactionId = transactionId;
        this.type = type;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
