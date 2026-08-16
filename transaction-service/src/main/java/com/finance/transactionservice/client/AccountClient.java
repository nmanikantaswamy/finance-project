package com.finance.transactionservice.client;

import com.finance.transactionservice.client.dto.AccountClientResponse;
import com.finance.transactionservice.client.dto.UpdateBalanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

    @GetMapping("/api/accounts/{accountId}")
    AccountClientResponse getAccountById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("accountId") Long accountId
    );

    @PutMapping("/api/accounts/{accountId}/balance")
    AccountClientResponse updateBalance(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("accountId") Long accountId,
            @RequestBody UpdateBalanceRequest request
    );
}