package com.lds.service.mapper;

import com.lds.domain.DocumentStatus;
import com.lds.service.dto.DocumentStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentStatus} and its DTO {@link DocumentStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentStatusMapper extends EntityMapper<DocumentStatusDTO, DocumentStatus> {}
