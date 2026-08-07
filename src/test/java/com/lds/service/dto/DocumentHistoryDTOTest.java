package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentHistoryDTO.class);
        DocumentHistoryDTO documentHistoryDTO1 = new DocumentHistoryDTO();
        documentHistoryDTO1.setId(1L);
        DocumentHistoryDTO documentHistoryDTO2 = new DocumentHistoryDTO();
        assertThat(documentHistoryDTO1).isNotEqualTo(documentHistoryDTO2);
        documentHistoryDTO2.setId(documentHistoryDTO1.getId());
        assertThat(documentHistoryDTO1).isEqualTo(documentHistoryDTO2);
        documentHistoryDTO2.setId(2L);
        assertThat(documentHistoryDTO1).isNotEqualTo(documentHistoryDTO2);
        documentHistoryDTO1.setId(null);
        assertThat(documentHistoryDTO1).isNotEqualTo(documentHistoryDTO2);
    }
}
