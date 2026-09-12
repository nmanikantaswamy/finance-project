package com.finance.reportingservice.repository;

import com.finance.reportingservice.entity.TransactionReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionReportRepository
        extends JpaRepository<TransactionReport, Long> {

    List<TransactionReport>
    findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TransactionReport>
    findByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<TransactionReport>
    findByTransactionTypeOrderByCreatedAtDesc(
            String transactionType
    );
}