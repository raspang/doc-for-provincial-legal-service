package com.lds.domain;

import static com.lds.domain.TransactionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionType.class);
        TransactionType transactionType1 = getTransactionTypeSample1();
        TransactionType transactionType2 = new TransactionType();
        assertThat(transactionType1).isNotEqualTo(transactionType2);

        transactionType2.setId(transactionType1.getId());
        assertThat(transactionType1).isEqualTo(transactionType2);

        transactionType2 = getTransactionTypeSample2();
        assertThat(transactionType1).isNotEqualTo(transactionType2);
    }
}
