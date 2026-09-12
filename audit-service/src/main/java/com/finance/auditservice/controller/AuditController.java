package com.finance.auditservice.controller;

import com.finance.auditservice.entity.AuditRecord;
import com.finance.auditservice.repository.AuditRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditRecordRepository auditRecordRepository;

    @GetMapping("/transactions/{transactionId}")
    public List<AuditRecord> getByTransactionId(
            @PathVariable Long transactionId) {

        return auditRecordRepository
                .findByTransactionId(transactionId);
    }

    @GetMapping("/users/{userId}")
    public List<AuditRecord> getByUserId(
            @PathVariable Long userId) {

        return auditRecordRepository
                .findByUserIdOrderByReceivedAtDesc(userId);
    }
}