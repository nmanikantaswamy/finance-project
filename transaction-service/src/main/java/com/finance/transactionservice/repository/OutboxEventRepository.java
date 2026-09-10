package com.finance.transactionservice.repository;

import com.finance.transactionservice.entity.OutboxEvent;
import com.finance.transactionservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(
            OutboxStatus status
    );
}