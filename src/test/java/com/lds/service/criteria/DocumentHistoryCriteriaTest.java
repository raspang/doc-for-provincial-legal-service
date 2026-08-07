package com.lds.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentHistoryCriteriaTest {

    @Test
    void newDocumentHistoryCriteriaHasAllFiltersNullTest() {
        var documentHistoryCriteria = new DocumentHistoryCriteria();
        assertThat(documentHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var documentHistoryCriteria = new DocumentHistoryCriteria();

        setAllFilters(documentHistoryCriteria);

        assertThat(documentHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentHistoryCriteriaCopyCreatesNullFilterTest() {
        var documentHistoryCriteria = new DocumentHistoryCriteria();
        var copy = documentHistoryCriteria.copy();

        assertThat(documentHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentHistoryCriteria)
        );
    }

    @Test
    void documentHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentHistoryCriteria = new DocumentHistoryCriteria();
        setAllFilters(documentHistoryCriteria);

        var copy = documentHistoryCriteria.copy();

        assertThat(documentHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentHistoryCriteria = new DocumentHistoryCriteria();

        assertThat(documentHistoryCriteria).hasToString("DocumentHistoryCriteria{}");
    }

    private static void setAllFilters(DocumentHistoryCriteria documentHistoryCriteria) {
        documentHistoryCriteria.id();
        documentHistoryCriteria.documentId();
        documentHistoryCriteria.documentType();
        documentHistoryCriteria.action();
        documentHistoryCriteria.changedBy();
        documentHistoryCriteria.timestamp();
        documentHistoryCriteria.previousValue();
        documentHistoryCriteria.newValue();
        documentHistoryCriteria.distinct();
    }

    private static Condition<DocumentHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDocumentType()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getChangedBy()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getPreviousValue()) &&
                condition.apply(criteria.getNewValue()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentHistoryCriteria> copyFiltersAre(
        DocumentHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDocumentType(), copy.getDocumentType()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getChangedBy(), copy.getChangedBy()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getPreviousValue(), copy.getPreviousValue()) &&
                condition.apply(criteria.getNewValue(), copy.getNewValue()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
