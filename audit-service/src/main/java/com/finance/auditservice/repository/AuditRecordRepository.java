package com.finance.auditservice.repository;

import com.finance.auditservice.entity.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRecordRepository
        extends JpaRepository<AuditRecord, Long> {

    List<AuditRecord> findByTransactionId(Long transactionId);

    List<AuditRecord> findByUserIdOrderByReceivedAtDesc(Long userId);
}