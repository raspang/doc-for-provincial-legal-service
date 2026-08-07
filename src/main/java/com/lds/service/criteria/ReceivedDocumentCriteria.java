package com.lds.service.criteria;

import com.lds.domain.enumeration.TransactionType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lds.domain.ReceivedDocument} entity. This class is used
 * in {@link com.lds.web.rest.ReceivedDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /received-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceivedDocumentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private StringFilter documentTitle;

    private TransactionTypeFilter transactionType;

    private IntegerFilter days;

    private InstantFilter dueDate;

    private IntegerFilter daysBeforeDue;

    private InstantFilter dateReleased;

    private StringFilter remarks;

    private LongFilter requestedActionId;

    private LongFilter typeOfDocumentId;

    private LongFilter officeId;

    private LongFilter responsiblePersonId;

    private LongFilter documentStatusId;

    private Boolean distinct;

    public ReceivedDocumentCriteria() {}

    public ReceivedDocumentCriteria(ReceivedDocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.documentTitle = other.optionalDocumentTitle().map(StringFilter::copy).orElse(null);
        this.transactionType = other.optionalTransactionType().map(TransactionTypeFilter::copy).orElse(null);
        this.days = other.optionalDays().map(IntegerFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.daysBeforeDue = other.optionalDaysBeforeDue().map(IntegerFilter::copy).orElse(null);
        this.dateReleased = other.optionalDateReleased().map(InstantFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.requestedActionId = other.optionalRequestedActionId().map(LongFilter::copy).orElse(null);
        this.typeOfDocumentId = other.optionalTypeOfDocumentId().map(LongFilter::copy).orElse(null);
        this.officeId = other.optionalOfficeId().map(LongFilter::copy).orElse(null);
        this.responsiblePersonId = other.optionalResponsiblePersonId().map(LongFilter::copy).orElse(null);
        this.documentStatusId = other.optionalDocumentStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReceivedDocumentCriteria copy() {
        return new ReceivedDocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDate() {
        return date;
    }

    public Optional<InstantFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public InstantFilter date() {
        if (date == null) {
            setDate(new InstantFilter());
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public StringFilter getDocumentTitle() {
        return documentTitle;
    }

    public Optional<StringFilter> optionalDocumentTitle() {
        return Optional.ofNullable(documentTitle);
    }

    public StringFilter documentTitle() {
        if (documentTitle == null) {
            setDocumentTitle(new StringFilter());
        }
        return documentTitle;
    }

    public void setDocumentTitle(StringFilter documentTitle) {
        this.documentTitle = documentTitle;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public Optional<TransactionTypeFilter> optionalTransactionType() {
        return Optional.ofNullable(transactionType);
    }

    public TransactionTypeFilter transactionType() {
        if (transactionType == null) {
            setTransactionType(new TransactionTypeFilter());
        }
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public IntegerFilter getDays() {
        return days;
    }

    public Optional<IntegerFilter> optionalDays() {
        return Optional.ofNullable(days);
    }

    public IntegerFilter days() {
        if (days == null) {
            setDays(new IntegerFilter());
        }
        return days;
    }

    public void setDays(IntegerFilter days) {
        this.days = days;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public Optional<InstantFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new InstantFilter());
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public IntegerFilter getDaysBeforeDue() {
        return daysBeforeDue;
    }

    public Optional<IntegerFilter> optionalDaysBeforeDue() {
        return Optional.ofNullable(daysBeforeDue);
    }

    public IntegerFilter daysBeforeDue() {
        if (daysBeforeDue == null) {
            setDaysBeforeDue(new IntegerFilter());
        }
        return daysBeforeDue;
    }

    public void setDaysBeforeDue(IntegerFilter daysBeforeDue) {
        this.daysBeforeDue = daysBeforeDue;
    }

    public InstantFilter getDateReleased() {
        return dateReleased;
    }

    public Optional<InstantFilter> optionalDateReleased() {
        return Optional.ofNullable(dateReleased);
    }

    public InstantFilter dateReleased() {
        if (dateReleased == null) {
            setDateReleased(new InstantFilter());
        }
        return dateReleased;
    }

    public void setDateReleased(InstantFilter dateReleased) {
        this.dateReleased = dateReleased;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public Optional<StringFilter> optionalRemarks() {
        return Optional.ofNullable(remarks);
    }

    public StringFilter remarks() {
        if (remarks == null) {
            setRemarks(new StringFilter());
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public LongFilter getRequestedActionId() {
        return requestedActionId;
    }

    public Optional<LongFilter> optionalRequestedActionId() {
        return Optional.ofNullable(requestedActionId);
    }

    public LongFilter requestedActionId() {
        if (requestedActionId == null) {
            setRequestedActionId(new LongFilter());
        }
        return requestedActionId;
    }

    public void setRequestedActionId(LongFilter requestedActionId) {
        this.requestedActionId = requestedActionId;
    }

    public LongFilter getTypeOfDocumentId() {
        return typeOfDocumentId;
    }

    public Optional<LongFilter> optionalTypeOfDocumentId() {
        return Optional.ofNullable(typeOfDocumentId);
    }

    public LongFilter typeOfDocumentId() {
        if (typeOfDocumentId == null) {
            setTypeOfDocumentId(new LongFilter());
        }
        return typeOfDocumentId;
    }

    public void setTypeOfDocumentId(LongFilter typeOfDocumentId) {
        this.typeOfDocumentId = typeOfDocumentId;
    }

    public LongFilter getOfficeId() {
        return officeId;
    }

    public Optional<LongFilter> optionalOfficeId() {
        return Optional.ofNullable(officeId);
    }

    public LongFilter officeId() {
        if (officeId == null) {
            setOfficeId(new LongFilter());
        }
        return officeId;
    }

    public void setOfficeId(LongFilter officeId) {
        this.officeId = officeId;
    }

    public LongFilter getResponsiblePersonId() {
        return responsiblePersonId;
    }

    public Optional<LongFilter> optionalResponsiblePersonId() {
        return Optional.ofNullable(responsiblePersonId);
    }

    public LongFilter responsiblePersonId() {
        if (responsiblePersonId == null) {
            setResponsiblePersonId(new LongFilter());
        }
        return responsiblePersonId;
    }

    public void setResponsiblePersonId(LongFilter responsiblePersonId) {
        this.responsiblePersonId = responsiblePersonId;
    }

    public LongFilter getDocumentStatusId() {
        return documentStatusId;
    }

    public Optional<LongFilter> optionalDocumentStatusId() {
        return Optional.ofNullable(documentStatusId);
    }

    public LongFilter documentStatusId() {
        if (documentStatusId == null) {
            setDocumentStatusId(new LongFilter());
        }
        return documentStatusId;
    }

    public void setDocumentStatusId(LongFilter documentStatusId) {
        this.documentStatusId = documentStatusId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReceivedDocumentCriteria that = (ReceivedDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(documentTitle, that.documentTitle) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(days, that.days) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(daysBeforeDue, that.daysBeforeDue) &&
            Objects.equals(dateReleased, that.dateReleased) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(requestedActionId, that.requestedActionId) &&
            Objects.equals(typeOfDocumentId, that.typeOfDocumentId) &&
            Objects.equals(officeId, that.officeId) &&
            Objects.equals(responsiblePersonId, that.responsiblePersonId) &&
            Objects.equals(documentStatusId, that.documentStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            date,
            documentTitle,
            transactionType,
            days,
            dueDate,
            daysBeforeDue,
            dateReleased,
            remarks,
            requestedActionId,
            typeOfDocumentId,
            officeId,
            responsiblePersonId,
            documentStatusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceivedDocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalDocumentTitle().map(f -> "documentTitle=" + f + ", ").orElse("") +
            optionalTransactionType().map(f -> "transactionType=" + f + ", ").orElse("") +
            optionalDays().map(f -> "days=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalDaysBeforeDue().map(f -> "daysBeforeDue=" + f + ", ").orElse("") +
            optionalDateReleased().map(f -> "dateReleased=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalRequestedActionId().map(f -> "requestedActionId=" + f + ", ").orElse("") +
            optionalTypeOfDocumentId().map(f -> "typeOfDocumentId=" + f + ", ").orElse("") +
            optionalOfficeId().map(f -> "officeId=" + f + ", ").orElse("") +
            optionalResponsiblePersonId().map(f -> "responsiblePersonId=" + f + ", ").orElse("") +
            optionalDocumentStatusId().map(f -> "documentStatusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
