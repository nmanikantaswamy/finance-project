package com.finance.reportingservice.controller;

import com.finance.reportingservice.entity.TransactionReport;
import com.finance.reportingservice.repository.TransactionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final TransactionReportRepository
            transactionReportRepository;

    @GetMapping("/users/{userId}/transactions")
    public List<TransactionReport> getUserTransactions(
            @PathVariable Long userId) {

        return transactionReportRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionReport> getAccountTransactions(
            @PathVariable Long accountId) {

        return transactionReportRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @GetMapping("/transactions/type/{type}")
    public List<TransactionReport> getTransactionsByType(
            @PathVariable String type) {

        return transactionReportRepository
                .findByTransactionTypeOrderByCreatedAtDesc(
                        type
                );
    }
}