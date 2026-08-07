package com.lds.service.mapper;

import static com.lds.domain.TypeOfDocumentAsserts.*;
import static com.lds.domain.TypeOfDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeOfDocumentMapperTest {

    private TypeOfDocumentMapper typeOfDocumentMapper;

    @BeforeEach
    void setUp() {
        typeOfDocumentMapper = new TypeOfDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeOfDocumentSample1();
        var actual = typeOfDocumentMapper.toEntity(typeOfDocumentMapper.toDto(expected));
        assertTypeOfDocumentAllPropertiesEquals(expected, actual);
    }
}
