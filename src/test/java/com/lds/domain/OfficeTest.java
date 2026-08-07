package com.lds.domain;

import static com.lds.domain.OfficeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfficeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Office.class);
        Office office1 = getOfficeSample1();
        Office office2 = new Office();
        assertThat(office1).isNotEqualTo(office2);

        office2.setId(office1.getId());
        assertThat(office1).isEqualTo(office2);

        office2 = getOfficeSample2();
        assertThat(office1).isNotEqualTo(office2);
    }
}
