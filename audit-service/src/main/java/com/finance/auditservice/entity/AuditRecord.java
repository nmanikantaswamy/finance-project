package com.finance.auditservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "audit_records",
        indexes = {
                @Index(
                        name = "idx_audit_transaction_id",
                        columnList = "transaction_id"
                ),
                @Index(
                        name = "idx_audit_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_audit_reference",
                        columnList = "transaction_reference"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(
            name = "transaction_reference",
            nullable = false,
            length = 100
    )
    private String transactionReference;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 30)
    private String status;

    @Lob
    @Column(name = "event_payload", nullable = false)
    private String eventPayload;

    @Column(name = "transaction_created_at")
    private LocalDateTime transactionCreatedAt;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;
}