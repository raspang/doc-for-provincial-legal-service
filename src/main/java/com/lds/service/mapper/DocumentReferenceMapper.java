package com.lds.service.mapper;

import com.lds.domain.DocumentReference;
import com.lds.domain.TypeOfDocument;
import com.lds.service.dto.DocumentReferenceDTO;
import com.lds.service.dto.TypeOfDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentReference} and its DTO {@link DocumentReferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentReferenceMapper extends EntityMapper<DocumentReferenceDTO, DocumentReference> {
    @Mapping(target = "typeOfDocument", source = "typeOfDocument", qualifiedByName = "typeOfDocumentName")
    DocumentReferenceDTO toDto(DocumentReference s);

    @Named("typeOfDocumentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TypeOfDocumentDTO toDtoTypeOfDocumentName(TypeOfDocument typeOfDocument);
}
