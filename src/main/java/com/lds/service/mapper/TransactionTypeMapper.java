package com.lds.service.mapper;

import com.lds.domain.TransactionType;
import com.lds.service.dto.TransactionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionType} and its DTO {@link TransactionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionTypeMapper extends EntityMapper<TransactionTypeDTO, TransactionType> {}
