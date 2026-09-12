package com.finance.reportingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.reportingservice.entity.TransactionReport;
import com.finance.reportingservice.repository.TransactionReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final ObjectMapper objectMapper;

    private final TransactionReportRepository
            transactionReportRepository;

    @KafkaListener(
            topics = "transaction-events",
            groupId = "reporting-service-group"
    )
    public void consumeTransactionEvent(String message) {

        try {

            TransactionEvent event =
                    objectMapper.readValue(
                            message,
                            TransactionEvent.class
                    );

            log.info(
                    "Received transaction for reporting: {}",
                    event.getTransactionReference()
            );

            TransactionReport report =
                    TransactionReport.builder()
                            .transactionId(event.getTransactionId())
                            .transactionReference(
                                    event.getTransactionReference()
                            )
                            .userId(event.getUserId())
                            .accountId(event.getAccountId())
                            .transactionType(event.getType())
                            .amount(event.getAmount())
                            .currency(event.getCurrency())
                            .status(event.getStatus())
                            .balanceAfter(event.getBalanceAfter())
                            .description(event.getDescription())
                            .createdAt(event.getCreatedAt())
                            .receivedAt(LocalDateTime.now())
                            .build();

            transactionReportRepository.save(report);

            log.info(
                    "Transaction report saved: {}",
                    event.getTransactionId()
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to process transaction report",
                    exception
            );
        }
    }
}