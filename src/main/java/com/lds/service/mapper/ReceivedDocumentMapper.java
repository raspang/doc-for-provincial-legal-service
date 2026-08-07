package com.lds.service.mapper;

import com.lds.domain.DocumentStatus;
import com.lds.domain.Office;
import com.lds.domain.ReceivedDocument;
import com.lds.domain.RequestedAction;
import com.lds.domain.ResponsiblePerson;
import com.lds.domain.TypeOfDocument;
import com.lds.service.dto.DocumentStatusDTO;
import com.lds.service.dto.OfficeDTO;
import com.lds.service.dto.ReceivedDocumentDTO;
import com.lds.service.dto.RequestedActionDTO;
import com.lds.service.dto.ResponsiblePersonDTO;
import com.lds.service.dto.TypeOfDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReceivedDocument} and its DTO {@link ReceivedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceivedDocumentMapper extends EntityMapper<ReceivedDocumentDTO, ReceivedDocument> {
    @Mapping(target = "requestedAction", source = "requestedAction", qualifiedByName = "requestedActionName")
    @Mapping(target = "typeOfDocument", source = "typeOfDocument", qualifiedByName = "typeOfDocumentName")
    @Mapping(target = "office", source = "office", qualifiedByName = "officeName")
    @Mapping(target = "responsiblePerson", source = "responsiblePerson", qualifiedByName = "responsiblePersonName")
    @Mapping(target = "documentStatus", source = "documentStatus", qualifiedByName = "documentStatusName")
    ReceivedDocumentDTO toDto(ReceivedDocument s);

    @Named("requestedActionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RequestedActionDTO toDtoRequestedActionName(RequestedAction requestedAction);

    @Named("typeOfDocumentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TypeOfDocumentDTO toDtoTypeOfDocumentName(TypeOfDocument typeOfDocument);

    @Named("officeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OfficeDTO toDtoOfficeName(Office office);

    @Named("responsiblePersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ResponsiblePersonDTO toDtoResponsiblePersonName(ResponsiblePerson responsiblePerson);

    @Named("documentStatusName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DocumentStatusDTO toDtoDocumentStatusName(DocumentStatus documentStatus);
}
