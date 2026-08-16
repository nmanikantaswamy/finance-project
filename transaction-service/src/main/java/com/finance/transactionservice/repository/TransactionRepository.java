package com.finance.transactionservice.repository;

import com.finance.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(
            Long accountId
    );

    Optional<Transaction> findByTransactionReference(
            String transactionReference
    );

    boolean existsByTransactionReference(
            String transactionReference
    );
}