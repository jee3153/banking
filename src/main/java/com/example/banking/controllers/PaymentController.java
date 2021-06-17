package com.example.banking.controllers;

import com.example.banking.entities.Transaction;
import com.example.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{payee}/to/{recipient}")
    ResponseEntity<Void> makePayment(@RequestBody Transaction payment, @PathVariable long payee, @PathVariable long recipient) {

        paymentService.makePayment(payment.getPayment(), payee, recipient);
        return ResponseEntity.ok().build();
    }
}
