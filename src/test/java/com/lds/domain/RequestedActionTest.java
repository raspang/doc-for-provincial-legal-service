package com.lds.domain;

import static com.lds.domain.RequestedActionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestedActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestedAction.class);
        RequestedAction requestedAction1 = getRequestedActionSample1();
        RequestedAction requestedAction2 = new RequestedAction();
        assertThat(requestedAction1).isNotEqualTo(requestedAction2);

        requestedAction2.setId(requestedAction1.getId());
        assertThat(requestedAction1).isEqualTo(requestedAction2);

        requestedAction2 = getRequestedActionSample2();
        assertThat(requestedAction1).isNotEqualTo(requestedAction2);
    }
}
