package com.finance.transactionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "outbox_events",
        indexes = {
                @Index(
                        name = "idx_outbox_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_outbox_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "aggregate_type",
            nullable = false,
            length = 100
    )
    private String aggregateType;

    @Column(
            name = "aggregate_id",
            nullable = false
    )
    private Long aggregateId;

    @Column(
            name = "event_type",
            nullable = false,
            length = 100
    )
    private String eventType;

    @Column(
            name = "event_key",
            nullable = false,
            unique = true,
            length = 100
    )
    private String eventKey;

    @Lob
    @Column(
            name = "payload",
            nullable = false,
            columnDefinition = "LONGTEXT"
    )
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private OutboxStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;
}