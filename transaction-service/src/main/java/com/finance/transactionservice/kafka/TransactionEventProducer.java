package com.finance.transactionservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private static final String TOPIC =
            "transaction-events";

    private final KafkaTemplate<String, TransactionEvent>
            kafkaTemplate;

    public void publish(TransactionEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getTransactionReference(),
                event
        );
    }
}