package com.lds.domain;

import static com.lds.domain.TypeOfDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfDocument.class);
        TypeOfDocument typeOfDocument1 = getTypeOfDocumentSample1();
        TypeOfDocument typeOfDocument2 = new TypeOfDocument();
        assertThat(typeOfDocument1).isNotEqualTo(typeOfDocument2);

        typeOfDocument2.setId(typeOfDocument1.getId());
        assertThat(typeOfDocument1).isEqualTo(typeOfDocument2);

        typeOfDocument2 = getTypeOfDocumentSample2();
        assertThat(typeOfDocument1).isNotEqualTo(typeOfDocument2);
    }
}
