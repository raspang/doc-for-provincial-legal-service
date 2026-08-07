package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceivedDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceivedDocumentDTO.class);
        ReceivedDocumentDTO receivedDocumentDTO1 = new ReceivedDocumentDTO();
        receivedDocumentDTO1.setId(1L);
        ReceivedDocumentDTO receivedDocumentDTO2 = new ReceivedDocumentDTO();
        assertThat(receivedDocumentDTO1).isNotEqualTo(receivedDocumentDTO2);
        receivedDocumentDTO2.setId(receivedDocumentDTO1.getId());
        assertThat(receivedDocumentDTO1).isEqualTo(receivedDocumentDTO2);
        receivedDocumentDTO2.setId(2L);
        assertThat(receivedDocumentDTO1).isNotEqualTo(receivedDocumentDTO2);
        receivedDocumentDTO1.setId(null);
        assertThat(receivedDocumentDTO1).isNotEqualTo(receivedDocumentDTO2);
    }
}
