package com.lds.web.rest;

import com.lds.service.DocumentDueSoonService;
import com.lds.service.dto.DocumentsDueSoonGroup;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/received-documents")
public class DocumentDueSoonResource {

    private final DocumentDueSoonService dueSoonService;

    public DocumentDueSoonResource(DocumentDueSoonService dueSoonService) {
        this.dueSoonService = dueSoonService;
    }

    /**
     * GET /api/received-documents/due-soon
     * Returns documents grouped by responsible person email (preview / dry-run).
     */
    @GetMapping("/due-soon")
    // @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<DocumentsDueSoonGroup>> getDocumentsDueSoon() {
        return ResponseEntity.ok(dueSoonService.findDocumentsDueSoon());
    }

    /**
     * POST /api/received-documents/due-soon/notify
     * Finds due-soon documents AND sends individual emails.
     */
    @PostMapping("/due-soon/notify")
    //  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DocumentsDueSoonGroup>> notifyDocumentsDueSoon() {
        return ResponseEntity.ok(dueSoonService.findAndNotifyDocumentsDueSoon());
    }
}
