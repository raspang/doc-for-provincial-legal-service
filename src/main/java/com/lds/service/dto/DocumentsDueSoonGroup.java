package com.lds.service.dto;

import java.time.Instant;
import java.util.List;

/**
 * Groups documents that are due soon / overdue, segregated by responsible person email.
 */
public record DocumentsDueSoonGroup(String email, String responsiblePersonName, List<DocumentDueSoonItem> documents) {
    public record DocumentDueSoonItem(
        Long id,
        Instant date,
        String documentTitle,
        String typeOfDocumentName,
        String officeName,
        String requestedActionName,
        String transactionTypeName,
        Integer targetDays,
        Instant dueDate,
        long daysBeforeDue,
        String documentStatusName,
        String remarks
    ) {}
}
