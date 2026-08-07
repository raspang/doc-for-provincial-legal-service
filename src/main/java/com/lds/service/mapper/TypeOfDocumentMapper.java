package com.lds.service.mapper;

import com.lds.domain.TypeOfDocument;
import com.lds.service.dto.TypeOfDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfDocument} and its DTO {@link TypeOfDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeOfDocumentMapper extends EntityMapper<TypeOfDocumentDTO, TypeOfDocument> {}
