package com.finance.transactionservice.kafka;

import com.finance.transactionservice.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String currency;

    private String status;

    private String description;

    private LocalDateTime createdAt;
}