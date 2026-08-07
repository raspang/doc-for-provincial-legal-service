package com.lds.service.mapper;

import static com.lds.domain.OfficeAsserts.*;
import static com.lds.domain.OfficeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfficeMapperTest {

    private OfficeMapper officeMapper;

    @BeforeEach
    void setUp() {
        officeMapper = new OfficeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOfficeSample1();
        var actual = officeMapper.toEntity(officeMapper.toDto(expected));
        assertOfficeAllPropertiesEquals(expected, actual);
    }
}
