package com.lds.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represents an outgoing transmittal or issued document (Outgoing).
 */
@Entity
@Table(name = "document_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReference extends AbstractAuditingEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "reference_no", unique = true, nullable = false, length = 20)
    private String referenceNo;

    @NotNull
    @Column(name = "document_title", nullable = false)
    private String documentTitle;

    @Column(name = "author")
    private String author;

    @Column(name = "date_released")
    private Instant dateReleased;

    @Column(name = "submitted_to_sir_king")
    private Instant submittedToSirKing;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    private TypeOfDocument typeOfDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentReference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public DocumentReference date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public DocumentReference referenceNo(String referenceNo) {
        this.setReferenceNo(referenceNo);
        return this;
    }

    protected void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getDocumentTitle() {
        return this.documentTitle;
    }

    public DocumentReference documentTitle(String documentTitle) {
        this.setDocumentTitle(documentTitle);
        return this;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getAuthor() {
        return this.author;
    }

    public DocumentReference author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Instant getDateReleased() {
        return this.dateReleased;
    }

    public DocumentReference dateReleased(Instant dateReleased) {
        this.setDateReleased(dateReleased);
        return this;
    }

    public void setDateReleased(Instant dateReleased) {
        this.dateReleased = dateReleased;
    }

    public Instant getSubmittedToSirKing() {
        return this.submittedToSirKing;
    }

    public DocumentReference submittedToSirKing(Instant submittedToSirKing) {
        this.setSubmittedToSirKing(submittedToSirKing);
        return this;
    }

    public void setSubmittedToSirKing(Instant submittedToSirKing) {
        this.submittedToSirKing = submittedToSirKing;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public DocumentReference remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TypeOfDocument getTypeOfDocument() {
        return this.typeOfDocument;
    }

    public void setTypeOfDocument(TypeOfDocument typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    public DocumentReference typeOfDocument(TypeOfDocument typeOfDocument) {
        this.setTypeOfDocument(typeOfDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReference)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentReference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReference{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", dateReleased='" + getDateReleased() + "'" +
            ", submittedToSirKing='" + getSubmittedToSirKing() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
