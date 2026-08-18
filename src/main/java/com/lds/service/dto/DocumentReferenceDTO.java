package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.DocumentReference} entity.
 */
@Schema(description = "Represents an outgoing transmittal or issued document (Outgoing).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReferenceDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    private String referenceNo;

    @NotNull
    private String documentTitle;

    private String author;

    private Instant dateReleased;

    private Instant submittedToSirKing;

    private String remarks;

    @NotNull
    private TypeOfDocumentDTO typeOfDocument;

    private Instant createdDate; // JHipster's AbstractAuditingEntity uses 'createdDate' instead of 'createdOn'
    private String createdBy;
    private Instant lastModifiedDate; // JHipster uses 'lastModifiedDate' instead of 'updatedOn'
    private String lastModifiedBy;

    // Getters and Setters
    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Instant getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Instant dateReleased) {
        this.dateReleased = dateReleased;
    }

    public Instant getSubmittedToSirKing() {
        return submittedToSirKing;
    }

    public void setSubmittedToSirKing(Instant submittedToSirKing) {
        this.submittedToSirKing = submittedToSirKing;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TypeOfDocumentDTO getTypeOfDocument() {
        return typeOfDocument;
    }

    public void setTypeOfDocument(TypeOfDocumentDTO typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReferenceDTO)) {
            return false;
        }

        DocumentReferenceDTO documentReferenceDTO = (DocumentReferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentReferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReferenceDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", dateReleased='" + getDateReleased() + "'" +
            ", submittedToSirKing='" + getSubmittedToSirKing() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", typeOfDocument=" + getTypeOfDocument() +
            "}";
    }
}
