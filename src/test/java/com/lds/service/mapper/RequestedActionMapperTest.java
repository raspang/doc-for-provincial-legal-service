package com.lds.service.mapper;

import static com.lds.domain.RequestedActionAsserts.*;
import static com.lds.domain.RequestedActionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestedActionMapperTest {

    private RequestedActionMapper requestedActionMapper;

    @BeforeEach
    void setUp() {
        requestedActionMapper = new RequestedActionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRequestedActionSample1();
        var actual = requestedActionMapper.toEntity(requestedActionMapper.toDto(expected));
        assertRequestedActionAllPropertiesEquals(expected, actual);
    }
}
