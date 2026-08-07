package com.lds.service.criteria;

import com.lds.domain.enumeration.DocumentType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lds.domain.DocumentHistory} entity. This class is used
 * in {@link com.lds.web.rest.DocumentHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentHistoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {

        public DocumentTypeFilter() {}

        public DocumentTypeFilter(DocumentTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocumentTypeFilter copy() {
            return new DocumentTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private DocumentTypeFilter documentType;

    private StringFilter action;

    private StringFilter changedBy;

    private InstantFilter timestamp;

    private StringFilter previousValue;

    private StringFilter newValue;

    private Boolean distinct;

    public DocumentHistoryCriteria() {}

    public DocumentHistoryCriteria(DocumentHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.documentType = other.optionalDocumentType().map(DocumentTypeFilter::copy).orElse(null);
        this.action = other.optionalAction().map(StringFilter::copy).orElse(null);
        this.changedBy = other.optionalChangedBy().map(StringFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.previousValue = other.optionalPreviousValue().map(StringFilter::copy).orElse(null);
        this.newValue = other.optionalNewValue().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentHistoryCriteria copy() {
        return new DocumentHistoryCriteria(this);
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

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public DocumentTypeFilter getDocumentType() {
        return documentType;
    }

    public Optional<DocumentTypeFilter> optionalDocumentType() {
        return Optional.ofNullable(documentType);
    }

    public DocumentTypeFilter documentType() {
        if (documentType == null) {
            setDocumentType(new DocumentTypeFilter());
        }
        return documentType;
    }

    public void setDocumentType(DocumentTypeFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getAction() {
        return action;
    }

    public Optional<StringFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public StringFilter action() {
        if (action == null) {
            setAction(new StringFilter());
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getChangedBy() {
        return changedBy;
    }

    public Optional<StringFilter> optionalChangedBy() {
        return Optional.ofNullable(changedBy);
    }

    public StringFilter changedBy() {
        if (changedBy == null) {
            setChangedBy(new StringFilter());
        }
        return changedBy;
    }

    public void setChangedBy(StringFilter changedBy) {
        this.changedBy = changedBy;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getPreviousValue() {
        return previousValue;
    }

    public Optional<StringFilter> optionalPreviousValue() {
        return Optional.ofNullable(previousValue);
    }

    public StringFilter previousValue() {
        if (previousValue == null) {
            setPreviousValue(new StringFilter());
        }
        return previousValue;
    }

    public void setPreviousValue(StringFilter previousValue) {
        this.previousValue = previousValue;
    }

    public StringFilter getNewValue() {
        return newValue;
    }

    public Optional<StringFilter> optionalNewValue() {
        return Optional.ofNullable(newValue);
    }

    public StringFilter newValue() {
        if (newValue == null) {
            setNewValue(new StringFilter());
        }
        return newValue;
    }

    public void setNewValue(StringFilter newValue) {
        this.newValue = newValue;
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
        final DocumentHistoryCriteria that = (DocumentHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(action, that.action) &&
            Objects.equals(changedBy, that.changedBy) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(previousValue, that.previousValue) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentId, documentType, action, changedBy, timestamp, previousValue, newValue, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDocumentType().map(f -> "documentType=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalChangedBy().map(f -> "changedBy=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalPreviousValue().map(f -> "previousValue=" + f + ", ").orElse("") +
            optionalNewValue().map(f -> "newValue=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
