package com.lds.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentReferenceCriteriaTest {

    @Test
    void newDocumentReferenceCriteriaHasAllFiltersNullTest() {
        var documentReferenceCriteria = new DocumentReferenceCriteria();
        assertThat(documentReferenceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentReferenceCriteriaFluentMethodsCreatesFiltersTest() {
        var documentReferenceCriteria = new DocumentReferenceCriteria();

        setAllFilters(documentReferenceCriteria);

        assertThat(documentReferenceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentReferenceCriteriaCopyCreatesNullFilterTest() {
        var documentReferenceCriteria = new DocumentReferenceCriteria();
        var copy = documentReferenceCriteria.copy();

        assertThat(documentReferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentReferenceCriteria)
        );
    }

    @Test
    void documentReferenceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentReferenceCriteria = new DocumentReferenceCriteria();
        setAllFilters(documentReferenceCriteria);

        var copy = documentReferenceCriteria.copy();

        assertThat(documentReferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentReferenceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentReferenceCriteria = new DocumentReferenceCriteria();

        assertThat(documentReferenceCriteria).hasToString("DocumentReferenceCriteria{}");
    }

    private static void setAllFilters(DocumentReferenceCriteria documentReferenceCriteria) {
        documentReferenceCriteria.id();
        documentReferenceCriteria.date();
        documentReferenceCriteria.referenceNo();
        documentReferenceCriteria.documentTitle();
        documentReferenceCriteria.author();
        documentReferenceCriteria.dateReleased();
        documentReferenceCriteria.submittedToSirKing();
        documentReferenceCriteria.remarks();
        documentReferenceCriteria.typeOfDocumentId();
        documentReferenceCriteria.distinct();
    }

    private static Condition<DocumentReferenceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getReferenceNo()) &&
                condition.apply(criteria.getDocumentTitle()) &&
                condition.apply(criteria.getAuthor()) &&
                condition.apply(criteria.getDateReleased()) &&
                condition.apply(criteria.getSubmittedToSirKing()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getTypeOfDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentReferenceCriteria> copyFiltersAre(
        DocumentReferenceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getReferenceNo(), copy.getReferenceNo()) &&
                condition.apply(criteria.getDocumentTitle(), copy.getDocumentTitle()) &&
                condition.apply(criteria.getAuthor(), copy.getAuthor()) &&
                condition.apply(criteria.getDateReleased(), copy.getDateReleased()) &&
                condition.apply(criteria.getSubmittedToSirKing(), copy.getSubmittedToSirKing()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getTypeOfDocumentId(), copy.getTypeOfDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
