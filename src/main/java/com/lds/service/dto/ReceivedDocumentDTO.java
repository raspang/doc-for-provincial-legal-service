package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.ReceivedDocument} entity.
 */
@Schema(description = "Represents a document received from an external agency or office (Incoming).")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceivedDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private String documentTitle;

    private Instant dateReleased;

    private String remarks;

    private RequestedActionDTO requestedAction;

    @NotNull
    private TypeOfDocumentDTO typeOfDocument;

    private OfficeDTO office;

    private ResponsiblePersonDTO responsiblePerson;

    @NotNull
    private DocumentStatusDTO documentStatus;

    private TransactionTypeDTO transactionType;

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

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Instant getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Instant dateReleased) {
        this.dateReleased = dateReleased;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public RequestedActionDTO getRequestedAction() {
        return requestedAction;
    }

    public void setRequestedAction(RequestedActionDTO requestedAction) {
        this.requestedAction = requestedAction;
    }

    public TypeOfDocumentDTO getTypeOfDocument() {
        return typeOfDocument;
    }

    public void setTypeOfDocument(TypeOfDocumentDTO typeOfDocument) {
        this.typeOfDocument = typeOfDocument;
    }

    public OfficeDTO getOffice() {
        return office;
    }

    public void setOffice(OfficeDTO office) {
        this.office = office;
    }

    public ResponsiblePersonDTO getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(ResponsiblePersonDTO responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public DocumentStatusDTO getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatusDTO documentStatus) {
        this.documentStatus = documentStatus;
    }

    public TransactionTypeDTO getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeDTO transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceivedDocumentDTO)) {
            return false;
        }

        ReceivedDocumentDTO receivedDocumentDTO = (ReceivedDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receivedDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceivedDocumentDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", dateReleased='" + getDateReleased() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", requestedAction=" + getRequestedAction() +
            ", typeOfDocument=" + getTypeOfDocument() +
            ", office=" + getOffice() +
            ", responsiblePerson=" + getResponsiblePerson() +
            ", documentStatus=" + getDocumentStatus() +
            ", transactionType=" + getTransactionType() +
            "}";
    }
}
