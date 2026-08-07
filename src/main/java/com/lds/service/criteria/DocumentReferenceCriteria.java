package com.lds.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.lds.domain.DocumentReference} entity. This class is used
 * in {@link com.lds.web.rest.DocumentReferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-references?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReferenceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private StringFilter referenceNo;

    private StringFilter documentTitle;

    private StringFilter author;

    private InstantFilter dateReleased;

    private InstantFilter submittedToSirKing;

    private StringFilter remarks;

    private LongFilter typeOfDocumentId;

    private Boolean distinct;

    public DocumentReferenceCriteria() {}

    public DocumentReferenceCriteria(DocumentReferenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.referenceNo = other.optionalReferenceNo().map(StringFilter::copy).orElse(null);
        this.documentTitle = other.optionalDocumentTitle().map(StringFilter::copy).orElse(null);
        this.author = other.optionalAuthor().map(StringFilter::copy).orElse(null);
        this.dateReleased = other.optionalDateReleased().map(InstantFilter::copy).orElse(null);
        this.submittedToSirKing = other.optionalSubmittedToSirKing().map(InstantFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.typeOfDocumentId = other.optionalTypeOfDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentReferenceCriteria copy() {
        return new DocumentReferenceCriteria(this);
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

    public StringFilter getReferenceNo() {
        return referenceNo;
    }

    public Optional<StringFilter> optionalReferenceNo() {
        return Optional.ofNullable(referenceNo);
    }

    public StringFilter referenceNo() {
        if (referenceNo == null) {
            setReferenceNo(new StringFilter());
        }
        return referenceNo;
    }

    public void setReferenceNo(StringFilter referenceNo) {
        this.referenceNo = referenceNo;
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

    public StringFilter getAuthor() {
        return author;
    }

    public Optional<StringFilter> optionalAuthor() {
        return Optional.ofNullable(author);
    }

    public StringFilter author() {
        if (author == null) {
            setAuthor(new StringFilter());
        }
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
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

    public InstantFilter getSubmittedToSirKing() {
        return submittedToSirKing;
    }

    public Optional<InstantFilter> optionalSubmittedToSirKing() {
        return Optional.ofNullable(submittedToSirKing);
    }

    public InstantFilter submittedToSirKing() {
        if (submittedToSirKing == null) {
            setSubmittedToSirKing(new InstantFilter());
        }
        return submittedToSirKing;
    }

    public void setSubmittedToSirKing(InstantFilter submittedToSirKing) {
        this.submittedToSirKing = submittedToSirKing;
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
        final DocumentReferenceCriteria that = (DocumentReferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(referenceNo, that.referenceNo) &&
            Objects.equals(documentTitle, that.documentTitle) &&
            Objects.equals(author, that.author) &&
            Objects.equals(dateReleased, that.dateReleased) &&
            Objects.equals(submittedToSirKing, that.submittedToSirKing) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(typeOfDocumentId, that.typeOfDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            date,
            referenceNo,
            documentTitle,
            author,
            dateReleased,
            submittedToSirKing,
            remarks,
            typeOfDocumentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReferenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalReferenceNo().map(f -> "referenceNo=" + f + ", ").orElse("") +
            optionalDocumentTitle().map(f -> "documentTitle=" + f + ", ").orElse("") +
            optionalAuthor().map(f -> "author=" + f + ", ").orElse("") +
            optionalDateReleased().map(f -> "dateReleased=" + f + ", ").orElse("") +
            optionalSubmittedToSirKing().map(f -> "submittedToSirKing=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalTypeOfDocumentId().map(f -> "typeOfDocumentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
