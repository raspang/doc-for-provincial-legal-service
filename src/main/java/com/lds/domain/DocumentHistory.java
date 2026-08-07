package com.lds.domain;

import com.lds.domain.enumeration.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tracks the history and audit trail for a specific document.
 */
@Entity
@Table(name = "document_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "previous_value")
    private String previousValue;

    @Column(name = "new_value")
    private String newValue;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public DocumentHistory documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public DocumentHistory documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getAction() {
        return this.action;
    }

    public DocumentHistory action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangedBy() {
        return this.changedBy;
    }

    public DocumentHistory changedBy(String changedBy) {
        this.setChangedBy(changedBy);
        return this;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public DocumentHistory timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPreviousValue() {
        return this.previousValue;
    }

    public DocumentHistory previousValue(String previousValue) {
        this.setPreviousValue(previousValue);
        return this;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public DocumentHistory newValue(String newValue) {
        this.setNewValue(newValue);
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public DocumentHistory remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentHistory{" +
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
