package com.lds.domain;

import static com.lds.domain.DocumentReferenceTestSamples.*;
import static com.lds.domain.TypeOfDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentReference.class);
        DocumentReference documentReference1 = getDocumentReferenceSample1();
        DocumentReference documentReference2 = new DocumentReference();
        assertThat(documentReference1).isNotEqualTo(documentReference2);

        documentReference2.setId(documentReference1.getId());
        assertThat(documentReference1).isEqualTo(documentReference2);

        documentReference2 = getDocumentReferenceSample2();
        assertThat(documentReference1).isNotEqualTo(documentReference2);
    }

    @Test
    void typeOfDocumentTest() {
        DocumentReference documentReference = getDocumentReferenceRandomSampleGenerator();
        TypeOfDocument typeOfDocumentBack = getTypeOfDocumentRandomSampleGenerator();

        documentReference.setTypeOfDocument(typeOfDocumentBack);
        assertThat(documentReference.getTypeOfDocument()).isEqualTo(typeOfDocumentBack);

        documentReference.typeOfDocument(null);
        assertThat(documentReference.getTypeOfDocument()).isNull();
    }
}
