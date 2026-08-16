package com.finance.transactionservice.client.dto;

import com.finance.transactionservice.client.dto.AccountType;
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
public class AccountClientResponse {

    private Long id;

    private String accountNumber;

    private Long userId;

    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private String status;
}