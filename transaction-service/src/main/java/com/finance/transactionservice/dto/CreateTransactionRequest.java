package com.finance.transactionservice.dto;

import com.finance.transactionservice.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than zero"
    )
    private BigDecimal amount;

    private String currency;

    private String description;

    /*
     * Used only for TRANSFER.
     * For DEPOSIT and WITHDRAW this can be null.
     */
    private Long destinationAccountId;
}