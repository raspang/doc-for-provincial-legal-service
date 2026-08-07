package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentStatusDTO.class);
        DocumentStatusDTO documentStatusDTO1 = new DocumentStatusDTO();
        documentStatusDTO1.setId(1L);
        DocumentStatusDTO documentStatusDTO2 = new DocumentStatusDTO();
        assertThat(documentStatusDTO1).isNotEqualTo(documentStatusDTO2);
        documentStatusDTO2.setId(documentStatusDTO1.getId());
        assertThat(documentStatusDTO1).isEqualTo(documentStatusDTO2);
        documentStatusDTO2.setId(2L);
        assertThat(documentStatusDTO1).isNotEqualTo(documentStatusDTO2);
        documentStatusDTO1.setId(null);
        assertThat(documentStatusDTO1).isNotEqualTo(documentStatusDTO2);
    }
}
