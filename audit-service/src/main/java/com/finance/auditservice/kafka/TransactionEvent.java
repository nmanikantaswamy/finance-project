package com.finance.auditservice.kafka;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEvent {

    private Long transactionId;
    private String transactionReference;
    private Long userId;
    private Long accountId;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String currency;
    private String status;
    private String description;
    private LocalDateTime createdAt;
}