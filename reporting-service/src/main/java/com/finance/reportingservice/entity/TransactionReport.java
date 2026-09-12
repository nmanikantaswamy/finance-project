package com.finance.reportingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "transaction_reports",
        indexes = {
                @Index(
                        name = "idx_report_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_report_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_report_type",
                        columnList = "transaction_type"
                ),
                @Index(
                        name = "idx_report_created",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transaction_id",
            nullable = false,
            unique = true
    )
    private Long transactionId;

    @Column(
            name = "transaction_reference",
            nullable = false
    )
    private String transactionReference;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(
            name = "transaction_type",
            nullable = false
    )
    private String transactionType;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String status;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}