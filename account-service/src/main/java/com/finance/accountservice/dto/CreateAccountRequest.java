package com.finance.accountservice.dto;

import com.finance.accountservice.entity.AccountType;
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
public class CreateAccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @DecimalMin(
            value = "0.00",
            message = "Initial deposit cannot be negative"
    )
    private BigDecimal initialDeposit;

    @jakarta.validation.constraints.Pattern(
            regexp = "INR|USD|EUR|GBP",
            message = "Currency must be INR, USD, EUR or GBP"
    )
    private String currency;
}