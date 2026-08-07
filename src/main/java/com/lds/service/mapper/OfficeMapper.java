package com.lds.service.mapper;

import com.lds.domain.Office;
import com.lds.service.dto.OfficeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Office} and its DTO {@link OfficeDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfficeMapper extends EntityMapper<OfficeDTO, Office> {}
