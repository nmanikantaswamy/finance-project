package com.finance.accountservice.dto;

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
public class UpdateBalanceRequest {

    @NotNull(message = "Balance is required")
    @DecimalMin(
            value = "0.00",
            message = "Balance cannot be negative"
    )
    private BigDecimal balance;
}