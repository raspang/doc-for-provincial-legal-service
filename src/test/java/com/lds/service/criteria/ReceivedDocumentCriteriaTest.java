package com.lds.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReceivedDocumentCriteriaTest {

    @Test
    void newReceivedDocumentCriteriaHasAllFiltersNullTest() {
        var receivedDocumentCriteria = new ReceivedDocumentCriteria();
        assertThat(receivedDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void receivedDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var receivedDocumentCriteria = new ReceivedDocumentCriteria();

        setAllFilters(receivedDocumentCriteria);

        assertThat(receivedDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void receivedDocumentCriteriaCopyCreatesNullFilterTest() {
        var receivedDocumentCriteria = new ReceivedDocumentCriteria();
        var copy = receivedDocumentCriteria.copy();

        assertThat(receivedDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(receivedDocumentCriteria)
        );
    }

    @Test
    void receivedDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var receivedDocumentCriteria = new ReceivedDocumentCriteria();
        setAllFilters(receivedDocumentCriteria);

        var copy = receivedDocumentCriteria.copy();

        assertThat(receivedDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(receivedDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var receivedDocumentCriteria = new ReceivedDocumentCriteria();

        assertThat(receivedDocumentCriteria).hasToString("ReceivedDocumentCriteria{}");
    }

    private static void setAllFilters(ReceivedDocumentCriteria receivedDocumentCriteria) {
        receivedDocumentCriteria.id();
        receivedDocumentCriteria.date();
        receivedDocumentCriteria.documentTitle();
        receivedDocumentCriteria.transactionType();
        receivedDocumentCriteria.days();
        receivedDocumentCriteria.dueDate();
        receivedDocumentCriteria.daysBeforeDue();
        receivedDocumentCriteria.dateReleased();
        receivedDocumentCriteria.remarks();
        receivedDocumentCriteria.requestedActionId();
        receivedDocumentCriteria.typeOfDocumentId();
        receivedDocumentCriteria.officeId();
        receivedDocumentCriteria.responsiblePersonId();
        receivedDocumentCriteria.documentStatusId();
        receivedDocumentCriteria.distinct();
    }

    private static Condition<ReceivedDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getDocumentTitle()) &&
                condition.apply(criteria.getTransactionType()) &&
                condition.apply(criteria.getDays()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getDaysBeforeDue()) &&
                condition.apply(criteria.getDateReleased()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getRequestedActionId()) &&
                condition.apply(criteria.getTypeOfDocumentId()) &&
                condition.apply(criteria.getOfficeId()) &&
                condition.apply(criteria.getResponsiblePersonId()) &&
                condition.apply(criteria.getDocumentStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReceivedDocumentCriteria> copyFiltersAre(
        ReceivedDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getDocumentTitle(), copy.getDocumentTitle()) &&
                condition.apply(criteria.getTransactionType(), copy.getTransactionType()) &&
                condition.apply(criteria.getDays(), copy.getDays()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getDaysBeforeDue(), copy.getDaysBeforeDue()) &&
                condition.apply(criteria.getDateReleased(), copy.getDateReleased()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getRequestedActionId(), copy.getRequestedActionId()) &&
                condition.apply(criteria.getTypeOfDocumentId(), copy.getTypeOfDocumentId()) &&
                condition.apply(criteria.getOfficeId(), copy.getOfficeId()) &&
                condition.apply(criteria.getResponsiblePersonId(), copy.getResponsiblePersonId()) &&
                condition.apply(criteria.getDocumentStatusId(), copy.getDocumentStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
