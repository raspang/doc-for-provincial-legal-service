package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestedActionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestedActionDTO.class);
        RequestedActionDTO requestedActionDTO1 = new RequestedActionDTO();
        requestedActionDTO1.setId(1L);
        RequestedActionDTO requestedActionDTO2 = new RequestedActionDTO();
        assertThat(requestedActionDTO1).isNotEqualTo(requestedActionDTO2);
        requestedActionDTO2.setId(requestedActionDTO1.getId());
        assertThat(requestedActionDTO1).isEqualTo(requestedActionDTO2);
        requestedActionDTO2.setId(2L);
        assertThat(requestedActionDTO1).isNotEqualTo(requestedActionDTO2);
        requestedActionDTO1.setId(null);
        assertThat(requestedActionDTO1).isNotEqualTo(requestedActionDTO2);
    }
}
