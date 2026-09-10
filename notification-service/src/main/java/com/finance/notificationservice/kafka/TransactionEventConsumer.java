package com.finance.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionEventConsumer {
    private final ObjectMapper objectMapper;
    @KafkaListener(
            topics = "transaction-events",
            groupId = "notification-service-group"
    )
    public void consumeTransactionEvent(
            String message) {

        try {

            TransactionEvent event =
                    objectMapper.readValue(
                            message,
                            TransactionEvent.class
                    );

        log.info(
                "Received transaction event: {}",
                event.getTransactionReference()
        );

        log.info(
                "Transaction Type: {}",
                event.getType()
        );

        log.info(
                "User ID: {}",
                event.getUserId()
        );

        log.info(
                "Amount: {} {}",
                event.getAmount(),
                event.getCurrency()
        );

            processNotification(event);
        } catch (Exception exception) {

            log.error(
                    "Failed to process transaction event",
                    exception
            );
        }
    }

    private void processNotification(
            TransactionEvent event) {

        String message =
                "Transaction "
                        + event.getType()
                        + " of "
                        + event.getAmount()
                        + " "
                        + event.getCurrency()
                        + " completed. Reference: "
                        + event.getTransactionReference();

        log.info(
                "NOTIFICATION: {}",
                message
        );
    }
}