package com.finance.accountservice.repository;

import com.finance.accountservice.entity.Account;
import com.finance.accountservice.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(
            String accountNumber
    );

    List<Account> findByUserId(
            Long userId
    );

    List<Account> findByUserIdAndStatus(
            Long userId,
            AccountStatus status
    );

    boolean existsByAccountNumber(
            String accountNumber
    );
}