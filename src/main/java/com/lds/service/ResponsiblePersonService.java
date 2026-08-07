package com.lds.service;

import com.lds.domain.ResponsiblePerson;
import com.lds.repository.ResponsiblePersonRepository;
import com.lds.service.dto.ResponsiblePersonDTO;
import com.lds.service.mapper.ResponsiblePersonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.ResponsiblePerson}.
 */
@Service
@Transactional
public class ResponsiblePersonService {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsiblePersonService.class);

    private final ResponsiblePersonRepository responsiblePersonRepository;

    private final ResponsiblePersonMapper responsiblePersonMapper;

    public ResponsiblePersonService(
        ResponsiblePersonRepository responsiblePersonRepository,
        ResponsiblePersonMapper responsiblePersonMapper
    ) {
        this.responsiblePersonRepository = responsiblePersonRepository;
        this.responsiblePersonMapper = responsiblePersonMapper;
    }

    /**
     * Save a responsiblePerson.
     *
     * @param responsiblePersonDTO the entity to save.
     * @return the persisted entity.
     */
    public ResponsiblePersonDTO save(ResponsiblePersonDTO responsiblePersonDTO) {
        LOG.debug("Request to save ResponsiblePerson : {}", responsiblePersonDTO);
        ResponsiblePerson responsiblePerson = responsiblePersonMapper.toEntity(responsiblePersonDTO);
        responsiblePerson = responsiblePersonRepository.save(responsiblePerson);
        return responsiblePersonMapper.toDto(responsiblePerson);
    }

    /**
     * Update a responsiblePerson.
     *
     * @param responsiblePersonDTO the entity to save.
     * @return the persisted entity.
     */
    public ResponsiblePersonDTO update(ResponsiblePersonDTO responsiblePersonDTO) {
        LOG.debug("Request to update ResponsiblePerson : {}", responsiblePersonDTO);
        ResponsiblePerson responsiblePerson = responsiblePersonMapper.toEntity(responsiblePersonDTO);
        responsiblePerson = responsiblePersonRepository.save(responsiblePerson);
        return responsiblePersonMapper.toDto(responsiblePerson);
    }

    /**
     * Partially update a responsiblePerson.
     *
     * @param responsiblePersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResponsiblePersonDTO> partialUpdate(ResponsiblePersonDTO responsiblePersonDTO) {
        LOG.debug("Request to partially update ResponsiblePerson : {}", responsiblePersonDTO);

        return responsiblePersonRepository
            .findById(responsiblePersonDTO.getId())
            .map(existingResponsiblePerson -> {
                responsiblePersonMapper.partialUpdate(existingResponsiblePerson, responsiblePersonDTO);

                return existingResponsiblePerson;
            })
            .map(responsiblePersonRepository::save)
            .map(responsiblePersonMapper::toDto);
    }

    /**
     * Get all the responsiblePeople.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResponsiblePersonDTO> findAll() {
        LOG.debug("Request to get all ResponsiblePeople");
        return responsiblePersonRepository
            .findAll()
            .stream()
            .map(responsiblePersonMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one responsiblePerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResponsiblePersonDTO> findOne(Long id) {
        LOG.debug("Request to get ResponsiblePerson : {}", id);
        return responsiblePersonRepository.findById(id).map(responsiblePersonMapper::toDto);
    }

    /**
     * Delete the responsiblePerson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ResponsiblePerson : {}", id);
        responsiblePersonRepository.deleteById(id);
    }
}
