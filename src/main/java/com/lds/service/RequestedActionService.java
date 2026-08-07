package com.lds.service;

import com.lds.domain.RequestedAction;
import com.lds.repository.RequestedActionRepository;
import com.lds.service.dto.RequestedActionDTO;
import com.lds.service.mapper.RequestedActionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.RequestedAction}.
 */
@Service
@Transactional
public class RequestedActionService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestedActionService.class);

    private final RequestedActionRepository requestedActionRepository;

    private final RequestedActionMapper requestedActionMapper;

    public RequestedActionService(RequestedActionRepository requestedActionRepository, RequestedActionMapper requestedActionMapper) {
        this.requestedActionRepository = requestedActionRepository;
        this.requestedActionMapper = requestedActionMapper;
    }

    /**
     * Save a requestedAction.
     *
     * @param requestedActionDTO the entity to save.
     * @return the persisted entity.
     */
    public RequestedActionDTO save(RequestedActionDTO requestedActionDTO) {
        LOG.debug("Request to save RequestedAction : {}", requestedActionDTO);
        RequestedAction requestedAction = requestedActionMapper.toEntity(requestedActionDTO);
        requestedAction = requestedActionRepository.save(requestedAction);
        return requestedActionMapper.toDto(requestedAction);
    }

    /**
     * Update a requestedAction.
     *
     * @param requestedActionDTO the entity to save.
     * @return the persisted entity.
     */
    public RequestedActionDTO update(RequestedActionDTO requestedActionDTO) {
        LOG.debug("Request to update RequestedAction : {}", requestedActionDTO);
        RequestedAction requestedAction = requestedActionMapper.toEntity(requestedActionDTO);
        requestedAction = requestedActionRepository.save(requestedAction);
        return requestedActionMapper.toDto(requestedAction);
    }

    /**
     * Partially update a requestedAction.
     *
     * @param requestedActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RequestedActionDTO> partialUpdate(RequestedActionDTO requestedActionDTO) {
        LOG.debug("Request to partially update RequestedAction : {}", requestedActionDTO);

        return requestedActionRepository
            .findById(requestedActionDTO.getId())
            .map(existingRequestedAction -> {
                requestedActionMapper.partialUpdate(existingRequestedAction, requestedActionDTO);

                return existingRequestedAction;
            })
            .map(requestedActionRepository::save)
            .map(requestedActionMapper::toDto);
    }

    /**
     * Get all the requestedActions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RequestedActionDTO> findAll() {
        LOG.debug("Request to get all RequestedActions");
        return requestedActionRepository
            .findAll()
            .stream()
            .map(requestedActionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one requestedAction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RequestedActionDTO> findOne(Long id) {
        LOG.debug("Request to get RequestedAction : {}", id);
        return requestedActionRepository.findById(id).map(requestedActionMapper::toDto);
    }

    /**
     * Delete the requestedAction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RequestedAction : {}", id);
        requestedActionRepository.deleteById(id);
    }
}
