package com.lds.service.mapper;

import static com.lds.domain.ReceivedDocumentAsserts.*;
import static com.lds.domain.ReceivedDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReceivedDocumentMapperTest {

    private ReceivedDocumentMapper receivedDocumentMapper;

    @BeforeEach
    void setUp() {
        receivedDocumentMapper = new ReceivedDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReceivedDocumentSample1();
        var actual = receivedDocumentMapper.toEntity(receivedDocumentMapper.toDto(expected));
        assertReceivedDocumentAllPropertiesEquals(expected, actual);
    }
}
