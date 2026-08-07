package com.lds.domain;

import static com.lds.domain.ResponsiblePersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponsiblePersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponsiblePerson.class);
        ResponsiblePerson responsiblePerson1 = getResponsiblePersonSample1();
        ResponsiblePerson responsiblePerson2 = new ResponsiblePerson();
        assertThat(responsiblePerson1).isNotEqualTo(responsiblePerson2);

        responsiblePerson2.setId(responsiblePerson1.getId());
        assertThat(responsiblePerson1).isEqualTo(responsiblePerson2);

        responsiblePerson2 = getResponsiblePersonSample2();
        assertThat(responsiblePerson1).isNotEqualTo(responsiblePerson2);
    }
}
