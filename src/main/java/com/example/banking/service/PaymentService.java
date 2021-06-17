package com.example.banking.service;

import com.example.banking.entities.Transaction;

import java.math.BigDecimal;

public interface PaymentService {
    void makePayment(BigDecimal amount, long payee, long recipient);
}
