package com.lds.domain;

import com.lds.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represents a document received from an external agency or office (Incoming).
 */
@Entity
@Table(name = "received_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceivedDocument implements Serializable {

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

    @NotNull
    @Column(name = "document_title", nullable = false)
    private String documentTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "days")
    private Integer days;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "days_before_due")
    private Integer daysBeforeDue;

    @Column(name = "date_released")
    private Instant dateReleased;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private RequestedAction requestedAction;

    @ManyToOne(optional = false)
    @NotNull
    private TypeOfDocument typeOfDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    private ResponsiblePerson responsiblePerson;

    @ManyToOne(optional = false)
    @NotNull
    private DocumentStatus documentStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReceivedDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public ReceivedDocument date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDocumentTitle() {
        return this.documentTitle;
    }

    public ReceivedDocument documentTitle(String documentTitle) {
        this.setDocumentTitle(documentTitle);
        return this;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public ReceivedDocument transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getDays() {
        return this.days;
    }

    public ReceivedDocument days(Integer days) {
        this.setDays(days);
        return this;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public ReceivedDocument dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDaysBeforeDue() {
        return this.daysBeforeDue;
    }

    public ReceivedDocument daysBeforeDue(Integer daysBeforeDue) {
        this.setDaysBeforeDue(daysBeforeDue);
        return this;
    }

    public void setDaysBeforeDue(Integer daysBeforeDue) {
        this.daysBeforeDue = daysBeforeDue;
    }

    public Instant getDateReleased() {
        return this.dateReleased;
    }

    public ReceivedDocument dateReleased(Instant dateReleased) {
        this.setDateReleased(dateReleased);
        return this;
    }

    public void setDateReleased(Instant dateReleased) {
        this.dateReleased = dateReleased;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public ReceivedDocument remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public RequestedAction getRequestedAction() {
        return this.requestedAction;
    }

    public void setRequestedAction(RequestedAction requestedAction) {
        this.requestedAction = requestedAction;
    }

    public ReceivedDocument requestedAction(RequestedAction requestedAction) {
        this.setRequestedAction(requestedAction);
        return this;
    }

    public TypeOfDocument getTypeOfDocument() {
        return this.typeOfDocument;
    }

    public void setTypeOfDocument(TypeOfDocument typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    public ReceivedDocument typeOfDocument(TypeOfDocument typeOfDocument) {
        this.setTypeOfDocument(typeOfDocument);
        return this;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public ReceivedDocument office(Office office) {
        this.setOffice(office);
        return this;
    }

    public ResponsiblePerson getResponsiblePerson() {
        return this.responsiblePerson;
    }

    public void setResponsiblePerson(ResponsiblePerson responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public ReceivedDocument responsiblePerson(ResponsiblePerson responsiblePerson) {
        this.setResponsiblePerson(responsiblePerson);
        return this;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public ReceivedDocument documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceivedDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((ReceivedDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceivedDocument{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", days=" + getDays() +
            ", dueDate='" + getDueDate() + "'" +
            ", daysBeforeDue=" + getDaysBeforeDue() +
            ", dateReleased='" + getDateReleased() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
