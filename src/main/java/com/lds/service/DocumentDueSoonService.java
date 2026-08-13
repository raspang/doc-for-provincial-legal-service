package com.lds.service;

import com.lds.domain.ReceivedDocument;
import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.dto.DocumentsDueSoonGroup;
import com.lds.service.dto.DocumentsDueSoonGroup.DocumentDueSoonItem;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DocumentDueSoonService {

    private static final Logger log = LoggerFactory.getLogger(DocumentDueSoonService.class);

    private final ReceivedDocumentRepository receivedDocumentRepository;
    private final DocumentNotificationService notificationService;

    public DocumentDueSoonService(ReceivedDocumentRepository receivedDocumentRepository, DocumentNotificationService notificationService) {
        this.receivedDocumentRepository = receivedDocumentRepository;
        this.notificationService = notificationService;
    }

    /**
     * Find all documents that are due soon (overdue or due today).
     *
     * Logic (mirrors received-document.ts):
     *   dueDate       = receivedDocument.date + transactionType.targetDays
     *   daysBeforeDue = dueDate - today  (in days)
     *   Filter:       documentStatus.warning == true AND daysBeforeDue <= 0
     *
     * @return list of DocumentsDueSoonGroup grouped by responsiblePerson.email
     */
    public List<DocumentsDueSoonGroup> findDocumentsDueSoon() {
        // 1. Fetch from DB
        List<ReceivedDocument> candidates = receivedDocumentRepository.findAllWithWarningAndResponsiblePerson();
        log.info("🔍 DB Query returned {} candidate documents.", candidates.size());

        if (candidates.isEmpty()) {
            log.warn(
                "⚠️ NO CANDIDATES FOUND. Check your DB: Does the document have warning=true, a responsible person, and a transaction type?"
            );
            return List.of();
        }

        LocalDate today = LocalDate.now();
        log.info("📅 Today's date is: {}", today);

        // 2. Filter by Date
        List<ReceivedDocument> overdueDocuments = candidates
            .stream()
            .filter(rd -> {
                Instant dueDate = computeDueDate(rd);
                if (dueDate == null) {
                    log.debug("  ❌ Skipping Doc {}: dueDate is null (missing date or targetDays)", rd.getId());
                    return false;
                }

                long daysBeforeDue = computeDaysBeforeDue(dueDate, today);
                log.info("  📄 Doc {} ('{}'): Due on {}, daysBeforeDue = {}", rd.getId(), rd.getDocumentTitle(), dueDate, daysBeforeDue);

                // TEMPORARY TEST: Changed from <= 0 to <= 30 to catch documents due in the future.
                // If you strictly only want OVERDUE documents, change this back to <= 0.
                return daysBeforeDue <= 30;
            })
            .toList();

        log.info("✅ After date filtering, {} documents remain.", overdueDocuments.size());

        if (overdueDocuments.isEmpty()) {
            return List.of();
        }

        // 3. Grouping logic remains the same...
        Map<String, List<ReceivedDocument>> groupedByEmail = overdueDocuments
            .stream()
            .collect(Collectors.groupingBy(rd -> rd.getResponsiblePerson().getEmail()));

        return groupedByEmail
            .entrySet()
            .stream()
            .map(entry -> {
                String email = entry.getKey();
                List<ReceivedDocument> docs = entry.getValue();
                String personName = docs.get(0).getResponsiblePerson().getName();
                List<DocumentDueSoonItem> items = docs
                    .stream()
                    .map(rd -> toDueSoonItem(rd, today))
                    .toList();
                return new DocumentsDueSoonGroup(email, personName, items);
            })
            .toList();
    }

    /**
     * Find documents due soon AND send email notifications individually.
     */
    public List<DocumentsDueSoonGroup> findAndNotifyDocumentsDueSoon() {
        List<DocumentsDueSoonGroup> groups = findDocumentsDueSoon();

        for (DocumentsDueSoonGroup group : groups) {
            try {
                notificationService.sendDueSoonNotification(group);
                log.info("Sent due-soon notification to {} ({} documents)", group.email(), group.documents().size());
            } catch (Exception e) {
                log.error("Failed to send due-soon notification to {}: {}", group.email(), e.getMessage());
            }
        }

        return groups;
    }

    // ─── Helpers ────────────────────────────────────────────────────────

    /**
     * dueDate = receivedDocument.date + transactionType.targetDays (days)
     * Mirrors: receivedDocument.date.add(targetDays, 'day') in TypeScript
     */
    private Instant computeDueDate(ReceivedDocument rd) {
        if (rd.getDate() == null) return null;
        if (rd.getTransactionType() == null) return null;

        Integer targetDays = rd.getTransactionType().getTargetDays();
        if (targetDays == null) return null;

        return rd.getDate().plus(targetDays, ChronoUnit.DAYS);
    }

    /**
     * daysBeforeDue = dueDate (start of day) - today (start of day) in days
     * Mirrors: due.diff(today, 'day') in TypeScript
     */
    private long computeDaysBeforeDue(Instant dueDate, LocalDate today) {
        LocalDate dueLocalDate = dueDate.atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(today, dueLocalDate);
    }

    private DocumentDueSoonItem toDueSoonItem(ReceivedDocument rd, LocalDate today) {
        Instant dueDate = computeDueDate(rd);
        long daysBeforeDue = computeDaysBeforeDue(dueDate, today);

        return new DocumentDueSoonItem(
            rd.getId(),
            rd.getDate(),
            rd.getDocumentTitle(),
            rd.getTypeOfDocument() != null ? rd.getTypeOfDocument().getName() : null,
            rd.getOffice() != null ? rd.getOffice().getName() : null,
            rd.getRequestedAction() != null ? rd.getRequestedAction().getName() : null,
            rd.getTransactionType() != null ? rd.getTransactionType().getName() : null,
            rd.getTransactionType() != null ? rd.getTransactionType().getTargetDays() : null,
            dueDate,
            daysBeforeDue,
            rd.getDocumentStatus() != null ? rd.getDocumentStatus().getName() : null,
            rd.getRemarks()
        );
    }
}
