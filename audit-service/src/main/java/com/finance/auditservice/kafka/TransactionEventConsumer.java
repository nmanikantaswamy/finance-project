package com.finance.auditservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.auditservice.entity.AuditRecord;
import com.finance.auditservice.repository.AuditRecordRepository;
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
    private final AuditRecordRepository auditRecordRepository;

    @KafkaListener(
            topics = "transaction-events",
            groupId = "audit-service-group"
    )
    public void consumeTransactionEvent(String message) {

        try {

            TransactionEvent event =
                    objectMapper.readValue(
                            message,
                            TransactionEvent.class
                    );

            log.info(
                    "Received transaction event for audit: {}",
                    event.getTransactionReference()
            );

            AuditRecord auditRecord =
                    AuditRecord.builder()
                            .transactionId(event.getTransactionId())
                            .transactionReference(
                                    event.getTransactionReference()
                            )
                            .userId(event.getUserId())
                            .accountId(event.getAccountId())
                            .eventType("TRANSACTION_COMPLETED")
                            .transactionType(event.getType())
                            .amount(event.getAmount())
                            .currency(event.getCurrency())
                            .status(event.getStatus())
                            .eventPayload(message)
                            .transactionCreatedAt(event.getCreatedAt())
                            .receivedAt(LocalDateTime.now())
                            .build();

            auditRecordRepository.save(auditRecord);

            log.info(
                    "Audit record saved: transactionId={}",
                    event.getTransactionId()
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to process transaction event for audit",
                    exception
            );
        }
    }
}