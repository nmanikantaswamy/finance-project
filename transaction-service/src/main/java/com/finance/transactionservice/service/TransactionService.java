package com.finance.transactionservice.service;

import com.finance.transactionservice.dto.CreateTransactionRequest;
import com.finance.transactionservice.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(
            Long userId,
            CreateTransactionRequest request
    );

    List<TransactionResponse> getUserTransactions(
            Long userId
    );

    List<TransactionResponse> getAccountTransactions(
            Long userId,
            Long accountId
    );

    TransactionResponse getTransactionById(
            Long userId,
            Long transactionId
    );
}