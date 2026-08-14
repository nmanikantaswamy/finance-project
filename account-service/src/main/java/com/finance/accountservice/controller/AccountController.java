package com.finance.accountservice.controller;

import com.finance.accountservice.dto.AccountResponse;
import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.dto.UpdateAccountRequest;
import com.finance.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response =
                accountService.createAccount(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @RequestHeader("X-User-Id") Long userId) {

        List<AccountResponse> accounts =
                accountService.getAccountsByUserId(
                        userId
                );

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long accountId) {

        AccountResponse response =
                accountService.getAccountById(
                        userId,
                        accountId
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountRequest request) {

        AccountResponse response =
                accountService.updateAccount(
                        userId,
                        accountId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long accountId) {

        accountService.deleteAccount(
                userId,
                accountId
        );

        return ResponseEntity.noContent().build();
    }
}