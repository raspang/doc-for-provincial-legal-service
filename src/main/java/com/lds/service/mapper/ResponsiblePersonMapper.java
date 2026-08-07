package com.lds.service.mapper;

import com.lds.domain.ResponsiblePerson;
import com.lds.service.dto.ResponsiblePersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponsiblePerson} and its DTO {@link ResponsiblePersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponsiblePersonMapper extends EntityMapper<ResponsiblePersonDTO, ResponsiblePerson> {}
