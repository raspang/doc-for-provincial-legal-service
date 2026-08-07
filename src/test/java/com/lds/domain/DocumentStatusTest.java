package com.lds.domain;

import static com.lds.domain.DocumentStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentStatus.class);
        DocumentStatus documentStatus1 = getDocumentStatusSample1();
        DocumentStatus documentStatus2 = new DocumentStatus();
        assertThat(documentStatus1).isNotEqualTo(documentStatus2);

        documentStatus2.setId(documentStatus1.getId());
        assertThat(documentStatus1).isEqualTo(documentStatus2);

        documentStatus2 = getDocumentStatusSample2();
        assertThat(documentStatus1).isNotEqualTo(documentStatus2);
    }
}
