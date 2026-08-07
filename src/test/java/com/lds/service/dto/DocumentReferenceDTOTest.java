package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentReferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentReferenceDTO.class);
        DocumentReferenceDTO documentReferenceDTO1 = new DocumentReferenceDTO();
        documentReferenceDTO1.setId(1L);
        DocumentReferenceDTO documentReferenceDTO2 = new DocumentReferenceDTO();
        assertThat(documentReferenceDTO1).isNotEqualTo(documentReferenceDTO2);
        documentReferenceDTO2.setId(documentReferenceDTO1.getId());
        assertThat(documentReferenceDTO1).isEqualTo(documentReferenceDTO2);
        documentReferenceDTO2.setId(2L);
        assertThat(documentReferenceDTO1).isNotEqualTo(documentReferenceDTO2);
        documentReferenceDTO1.setId(null);
        assertThat(documentReferenceDTO1).isNotEqualTo(documentReferenceDTO2);
    }
}
