package com.finance.transactionservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.transactionservice.entity.OutboxEvent;
import com.finance.transactionservice.entity.OutboxStatus;
import com.finance.transactionservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

    private static final String TOPIC =
            "transaction-events";

    private final OutboxEventRepository
            outboxEventRepository;

    private final KafkaTemplate<String, String>
            kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxEventRepository
                        .findTop100ByStatusOrderByCreatedAtAsc(
                                OutboxStatus.PENDING
                        );

        for (OutboxEvent event : events) {

            try {

                kafkaTemplate
                        .send(
                                TOPIC,
                                event.getEventKey(),
                                event.getPayload()
                        )
                        .get();

                event.setStatus(
                        OutboxStatus.PUBLISHED
                );

                event.setPublishedAt(
                        LocalDateTime.now()
                );

                outboxEventRepository.save(event);

                log.info(
                        "Published outbox event: {}",
                        event.getEventKey()
                );

            } catch (Exception exception) {

                int retryCount =
                        event.getRetryCount() == null
                                ? 0
                                : event.getRetryCount();

                event.setRetryCount(
                        retryCount + 1
                );

                if (retryCount + 1 >= 5) {

                    event.setStatus(
                            OutboxStatus.FAILED
                    );
                }

                outboxEventRepository.save(event);

                log.error(
                        "Failed to publish outbox event: {}",
                        event.getEventKey(),
                        exception
                );
            }
        }
    }
}