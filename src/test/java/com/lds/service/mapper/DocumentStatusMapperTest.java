package com.lds.service.mapper;

import static com.lds.domain.DocumentStatusAsserts.*;
import static com.lds.domain.DocumentStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentStatusMapperTest {

    private DocumentStatusMapper documentStatusMapper;

    @BeforeEach
    void setUp() {
        documentStatusMapper = new DocumentStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentStatusSample1();
        var actual = documentStatusMapper.toEntity(documentStatusMapper.toDto(expected));
        assertDocumentStatusAllPropertiesEquals(expected, actual);
    }
}
