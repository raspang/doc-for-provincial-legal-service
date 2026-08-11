package com.lds.service.mapper;

import static com.lds.domain.TransactionTypeAsserts.*;
import static com.lds.domain.TransactionTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionTypeMapperTest {

    private TransactionTypeMapper transactionTypeMapper;

    @BeforeEach
    void setUp() {
        transactionTypeMapper = new TransactionTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransactionTypeSample1();
        var actual = transactionTypeMapper.toEntity(transactionTypeMapper.toDto(expected));
        assertTransactionTypeAllPropertiesEquals(expected, actual);
    }
}
