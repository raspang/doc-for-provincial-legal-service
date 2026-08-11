package com.lds.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionTypeDTO.class);
        TransactionTypeDTO transactionTypeDTO1 = new TransactionTypeDTO();
        transactionTypeDTO1.setId(1L);
        TransactionTypeDTO transactionTypeDTO2 = new TransactionTypeDTO();
        assertThat(transactionTypeDTO1).isNotEqualTo(transactionTypeDTO2);
        transactionTypeDTO2.setId(transactionTypeDTO1.getId());
        assertThat(transactionTypeDTO1).isEqualTo(transactionTypeDTO2);
        transactionTypeDTO2.setId(2L);
        assertThat(transactionTypeDTO1).isNotEqualTo(transactionTypeDTO2);
        transactionTypeDTO1.setId(null);
        assertThat(transactionTypeDTO1).isNotEqualTo(transactionTypeDTO2);
    }
}
