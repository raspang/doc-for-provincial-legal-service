package com.lds.service.mapper;

import static com.lds.domain.DocumentHistoryAsserts.*;
import static com.lds.domain.DocumentHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentHistoryMapperTest {

    private DocumentHistoryMapper documentHistoryMapper;

    @BeforeEach
    void setUp() {
        documentHistoryMapper = new DocumentHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentHistorySample1();
        var actual = documentHistoryMapper.toEntity(documentHistoryMapper.toDto(expected));
        assertDocumentHistoryAllPropertiesEquals(expected, actual);
    }
}
