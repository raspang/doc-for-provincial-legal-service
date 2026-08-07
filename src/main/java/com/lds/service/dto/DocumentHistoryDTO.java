package com.lds.service.dto;

import com.lds.domain.enumeration.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.DocumentHistory} entity.
 */
@Schema(description = "Tracks the history and audit trail for a specific document.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private DocumentType documentType;

    @NotNull
    private String action;

    @NotNull
    private String changedBy;

    @NotNull
    private Instant timestamp;

    private String previousValue;

    private String newValue;

    @Lob
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentHistoryDTO)) {
            return false;
        }

        DocumentHistoryDTO documentHistoryDTO = (DocumentHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentHistoryDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentType='" + getDocumentType() + "'" +
            ", action='" + getAction() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", previousValue='" + getPreviousValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
