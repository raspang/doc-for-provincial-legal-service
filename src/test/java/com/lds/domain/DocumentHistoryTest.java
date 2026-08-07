package com.lds.domain;

import static com.lds.domain.DocumentHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentHistory.class);
        DocumentHistory documentHistory1 = getDocumentHistorySample1();
        DocumentHistory documentHistory2 = new DocumentHistory();
        assertThat(documentHistory1).isNotEqualTo(documentHistory2);

        documentHistory2.setId(documentHistory1.getId());
        assertThat(documentHistory1).isEqualTo(documentHistory2);

        documentHistory2 = getDocumentHistorySample2();
        assertThat(documentHistory1).isNotEqualTo(documentHistory2);
    }
}
