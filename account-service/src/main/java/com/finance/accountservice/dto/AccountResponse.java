package com.finance.accountservice.dto;

import com.finance.accountservice.entity.AccountStatus;
import com.finance.accountservice.entity.AccountType;
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
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private Long userId;

    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}