package com.finance.transactionservice.service.impl;

import com.finance.transactionservice.client.AccountClient;
import com.finance.transactionservice.client.dto.AccountClientResponse;
import com.finance.transactionservice.dto.CreateTransactionRequest;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.entity.Transaction;
import com.finance.transactionservice.entity.TransactionStatus;
import com.finance.transactionservice.entity.TransactionType;
import com.finance.transactionservice.repository.TransactionRepository;
import com.finance.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finance.transactionservice.kafka.TransactionEvent;
import com.finance.transactionservice.kafka.TransactionEventProducer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final TransactionEventProducer transactionEventProducer;

    @Override
    @Transactional
    public TransactionResponse createTransaction(
            Long userId,
            CreateTransactionRequest request) {

        return switch (request.getType()) {

            case DEPOSIT ->
                    processDeposit(userId, request);

            case WITHDRAW ->
                    processWithdrawal(userId, request);

            case TRANSFER ->
                    processTransfer(userId, request);
        };
    }

    @Override
    public List<TransactionResponse> getUserTransactions(
            Long userId) {

        return transactionRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TransactionResponse> getAccountTransactions(
            Long userId,
            Long accountId) {

        AccountClientResponse account =
                accountClient.getAccountById(
                        userId,
                        accountId
                );

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TransactionResponse getTransactionById(
            Long userId,
            Long transactionId) {

        Transaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You do not have access to this transaction"
            );
        }

        return mapToResponse(transaction);
    }

    private TransactionResponse processDeposit(
            Long userId,
            CreateTransactionRequest request) {

        AccountClientResponse account =
                accountClient.getAccountById(
                        userId,
                        request.getAccountId()
                );

        BigDecimal currentBalance =
                account.getBalance();

        BigDecimal newBalance =
                currentBalance.add(
                        request.getAmount()
                );

        AccountClientResponse updatedAccount =
                accountClient.updateBalance(
                        userId,
                        request.getAccountId(),
                        new com.finance.transactionservice.client.dto.UpdateBalanceRequest(
                                newBalance
                        )
                );

        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                generateReference()
                        )
                        .userId(userId)
                        .accountId(account.getId())
                        .type(TransactionType.DEPOSIT)
                        .amount(request.getAmount())
                        .balanceAfter(updatedAccount.getBalance())
                        .currency(
                                request.getCurrency() != null
                                        ? request.getCurrency()
                                        : account.getCurrency()
                        )
                        .status(TransactionStatus.COMPLETED)
                        .description(request.getDescription())
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        publishTransactionEvent(savedTransaction);

        return mapToResponse(savedTransaction);
    }

    private TransactionResponse processWithdrawal(
            Long userId,
            CreateTransactionRequest request) {

        AccountClientResponse account =
                accountClient.getAccountById(
                        userId,
                        request.getAccountId()
                );

        BigDecimal currentBalance =
                account.getBalance();

        if (currentBalance.compareTo(
                request.getAmount()) < 0) {

            throw new IllegalArgumentException(
                    "Insufficient account balance"
            );
        }

        BigDecimal newBalance =
                currentBalance.subtract(
                        request.getAmount()
                );

        AccountClientResponse updatedAccount =
                accountClient.updateBalance(
                        userId,
                        request.getAccountId(),
                        new com.finance.transactionservice.client.dto.UpdateBalanceRequest(
                                newBalance
                        )
                );

        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                generateReference()
                        )
                        .userId(userId)
                        .accountId(account.getId())
                        .type(TransactionType.WITHDRAW)
                        .amount(request.getAmount())
                        .balanceAfter(updatedAccount.getBalance())
                        .currency(
                                request.getCurrency() != null
                                        ? request.getCurrency()
                                        : account.getCurrency()
                        )
                        .status(TransactionStatus.COMPLETED)
                        .description(request.getDescription())
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        publishTransactionEvent(savedTransaction);

        return mapToResponse(savedTransaction);
    }

    private TransactionResponse processTransfer(
            Long userId,
            CreateTransactionRequest request) {

        if (request.getDestinationAccountId() == null) {

            throw new IllegalArgumentException(
                    "Destination account is required for transfer"
            );
        }

        if (request.getAccountId()
                .equals(request.getDestinationAccountId())) {

            throw new IllegalArgumentException(
                    "Source and destination accounts cannot be the same"
            );
        }

        AccountClientResponse sourceAccount =
                accountClient.getAccountById(
                        userId,
                        request.getAccountId()
                );

        AccountClientResponse destinationAccount =
                accountClient.getAccountInternal(
                        request.getDestinationAccountId()
                );

        if (sourceAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new IllegalArgumentException(
                    "Insufficient account balance"
            );
        }

        BigDecimal sourceNewBalance =
                sourceAccount.getBalance()
                        .subtract(request.getAmount());

        BigDecimal destinationNewBalance =
                destinationAccount.getBalance()
                        .add(request.getAmount());

        AccountClientResponse updatedSource =
                accountClient.updateBalance(
                        userId,
                        sourceAccount.getId(),
                        new com.finance.transactionservice.client.dto.UpdateBalanceRequest(
                                sourceNewBalance
                        )
                );

        accountClient.updateBalance(
                userId,
                destinationAccount.getId(),
                new com.finance.transactionservice.client.dto.UpdateBalanceRequest(
                        destinationNewBalance
                )
        );

        Transaction transaction =
                Transaction.builder()
                        .transactionReference(
                                generateReference()
                        )
                        .userId(userId)
                        .accountId(sourceAccount.getId())
                        .type(TransactionType.TRANSFER)
                        .amount(request.getAmount())
                        .balanceAfter(updatedSource.getBalance())
                        .currency(
                                request.getCurrency() != null
                                        ? request.getCurrency()
                                        : sourceAccount.getCurrency()
                        )
                        .status(TransactionStatus.COMPLETED)
                        .description(request.getDescription())
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        publishTransactionEvent(savedTransaction);

        return mapToResponse(savedTransaction);
    }

    private String generateReference() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }

    private TransactionResponse mapToResponse(
            Transaction transaction) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .userId(transaction.getUserId())
                .accountId(transaction.getAccountId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    private void publishTransactionEvent(
            Transaction transaction) {

        TransactionEvent event =
                TransactionEvent.builder()
                        .transactionId(
                                transaction.getId()
                        )
                        .transactionReference(
                                transaction.getTransactionReference()
                        )
                        .userId(
                                transaction.getUserId()
                        )
                        .accountId(
                                transaction.getAccountId()
                        )
                        .type(
                                transaction.getType()
                        )
                        .amount(
                                transaction.getAmount()
                        )
                        .balanceAfter(
                                transaction.getBalanceAfter()
                        )
                        .currency(
                                transaction.getCurrency()
                        )
                        .status(
                                transaction.getStatus().name()
                        )
                        .description(
                                transaction.getDescription()
                        )
                        .createdAt(
                                transaction.getCreatedAt()
                        )
                        .build();

        transactionEventProducer.publish(event);
    }
}