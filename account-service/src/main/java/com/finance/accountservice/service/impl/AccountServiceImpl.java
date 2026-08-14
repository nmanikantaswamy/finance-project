package com.finance.accountservice.service.impl;

import com.finance.accountservice.dto.AccountResponse;
import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.dto.UpdateAccountRequest;
import com.finance.accountservice.entity.Account;
import com.finance.accountservice.entity.AccountStatus;
import com.finance.accountservice.repository.AccountRepository;
import com.finance.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.finance.accountservice.exception.AccountAccessDeniedException;
import com.finance.accountservice.exception.AccountNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(
            Long userId,
            CreateAccountRequest request) {

        String accountNumber =
                generateUniqueAccountNumber();

        BigDecimal initialDeposit =
                request.getInitialDeposit();

        if (initialDeposit == null) {
            initialDeposit = BigDecimal.ZERO;
        }

        String currency =
                request.getCurrency();

        if (currency == null ||
                currency.isBlank()) {

            currency = "INR";
        }

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .userId(userId)
                .accountType(request.getAccountType())
                .balance(initialDeposit)
                .currency(currency)
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount =
                accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(
            Long userId) {

        return accountRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(
            Long userId,
            Long accountId) {

        Account account =
                accountRepository.findById(accountId)
                        .orElseThrow(() ->
                            new AccountNotFoundException(
                              "Account not found"
                            )
                        );

        if (!account.getUserId().equals(userId)) {

            throw new AccountAccessDeniedException(
                    "You do not have access to this account"
            );
        }

        return mapToResponse(account);
    }

    @Override
    public AccountResponse updateAccount(
            Long userId,
            Long accountId,
            UpdateAccountRequest request) {

        Account account =
                accountRepository.findById(accountId)
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "Account not found"
                                )
                        );

        if (!account.getUserId().equals(userId)) {

            throw new AccountAccessDeniedException(
                    "You do not have access to this account"
            );
        }

        account.setStatus(
                request.getStatus()
        );

        Account updatedAccount =
                accountRepository.save(account);

        return mapToResponse(updatedAccount);
    }

    @Override
    public void deleteAccount(
            Long userId,
            Long accountId) {

        Account account =
                accountRepository.findById(accountId)
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "Account not found"
                                )
                        );

        if (!account.getUserId().equals(userId)) {

            throw new AccountAccessDeniedException(
                    "You do not have access to this account"
            );
        }

        accountRepository.delete(account);
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {

            long randomNumber =
                    ThreadLocalRandom.current()
                            .nextLong(
                                    10_000_000L,
                                    100_000_000L
                            );

            accountNumber =
                    "ACC" + randomNumber;

        } while (
                accountRepository
                        .existsByAccountNumber(accountNumber)
        );

        return accountNumber;
    }

    private AccountResponse mapToResponse(
            Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(
                        account.getAccountNumber()
                )
                .userId(account.getUserId())
                .accountType(
                        account.getAccountType()
                )
                .balance(
                        account.getBalance()
                )
                .currency(
                        account.getCurrency()
                )
                .status(
                        account.getStatus()
                )
                .createdAt(
                        account.getCreatedAt()
                )
                .updatedAt(
                        account.getUpdatedAt()
                )
                .build();
    }
}