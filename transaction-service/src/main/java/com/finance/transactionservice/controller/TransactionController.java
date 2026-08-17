package com.finance.transactionservice.controller;

import com.finance.transactionservice.dto.CreateTransactionRequest;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateTransactionRequest request) {

        TransactionResponse response =
                transactionService.createTransaction(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>>
    getMyTransactions(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                transactionService.getUserTransactions(userId)
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>>
    getAccountTransactions(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                transactionService.getAccountTransactions(
                        userId,
                        accountId
                )
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse>
    getTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransactionById(
                        userId,
                        transactionId
                )
        );
    }
}