package com.lds.service.mapper;

import com.lds.domain.RequestedAction;
import com.lds.service.dto.RequestedActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestedAction} and its DTO {@link RequestedActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestedActionMapper extends EntityMapper<RequestedActionDTO, RequestedAction> {}
