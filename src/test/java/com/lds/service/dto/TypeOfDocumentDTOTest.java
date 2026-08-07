package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfDocumentDTO.class);
        TypeOfDocumentDTO typeOfDocumentDTO1 = new TypeOfDocumentDTO();
        typeOfDocumentDTO1.setId(1L);
        TypeOfDocumentDTO typeOfDocumentDTO2 = new TypeOfDocumentDTO();
        assertThat(typeOfDocumentDTO1).isNotEqualTo(typeOfDocumentDTO2);
        typeOfDocumentDTO2.setId(typeOfDocumentDTO1.getId());
        assertThat(typeOfDocumentDTO1).isEqualTo(typeOfDocumentDTO2);
        typeOfDocumentDTO2.setId(2L);
        assertThat(typeOfDocumentDTO1).isNotEqualTo(typeOfDocumentDTO2);
        typeOfDocumentDTO1.setId(null);
        assertThat(typeOfDocumentDTO1).isNotEqualTo(typeOfDocumentDTO2);
    }
}
