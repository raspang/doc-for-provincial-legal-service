package com.lds.service.mapper;

import com.lds.domain.DocumentHistory;
import com.lds.service.dto.DocumentHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentHistory} and its DTO {@link DocumentHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentHistoryMapper extends EntityMapper<DocumentHistoryDTO, DocumentHistory> {}
