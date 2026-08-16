package com.finance.transactionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(
                        name = "idx_transaction_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_transaction_account",
                        columnList = "account_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transaction_reference",
            nullable = false,
            unique = true,
            length = 50
    )
    private String transactionReference;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;

    @Column(
            name = "account_id",
            nullable = false
    )
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private TransactionType type;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceAfter;

    @Column(
            nullable = false,
            length = 3
    )
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private TransactionStatus status;

    @Column(length = 255)
    private String description;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
    }
}