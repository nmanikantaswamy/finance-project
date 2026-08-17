package com.finance.accountservice.service;

import com.finance.accountservice.dto.AccountResponse;
import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.dto.UpdateAccountRequest;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse createAccount(
            Long userId,
            CreateAccountRequest request
    );

    List<AccountResponse> getAccountsByUserId(
            Long userId
    );

    AccountResponse getAccountById(
            Long userId,
            Long accountId
    );

    AccountResponse updateAccount(
            Long userId,
            Long accountId,
            UpdateAccountRequest request
    );

    void deleteAccount(
            Long userId,
            Long accountId
    );
    AccountResponse updateBalance(
            Long userId,
            Long accountId,
            BigDecimal balance
    );
    AccountResponse getAccountInternal(Long accountId);
}