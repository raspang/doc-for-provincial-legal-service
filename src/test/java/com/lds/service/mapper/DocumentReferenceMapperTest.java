package com.lds.service.mapper;

import static com.lds.domain.DocumentReferenceAsserts.*;
import static com.lds.domain.DocumentReferenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentReferenceMapperTest {

    private DocumentReferenceMapper documentReferenceMapper;

    @BeforeEach
    void setUp() {
        documentReferenceMapper = new DocumentReferenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentReferenceSample1();
        var actual = documentReferenceMapper.toEntity(documentReferenceMapper.toDto(expected));
        assertDocumentReferenceAllPropertiesEquals(expected, actual);
    }
}
