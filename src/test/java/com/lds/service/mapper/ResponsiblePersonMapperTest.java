package com.lds.service.mapper;

import static com.lds.domain.ResponsiblePersonAsserts.*;
import static com.lds.domain.ResponsiblePersonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponsiblePersonMapperTest {

    private ResponsiblePersonMapper responsiblePersonMapper;

    @BeforeEach
    void setUp() {
        responsiblePersonMapper = new ResponsiblePersonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResponsiblePersonSample1();
        var actual = responsiblePersonMapper.toEntity(responsiblePersonMapper.toDto(expected));
        assertResponsiblePersonAllPropertiesEquals(expected, actual);
    }
}
