package com.lds.service;

import com.lds.domain.TransactionType;
import com.lds.repository.TransactionTypeRepository;
import com.lds.service.dto.TransactionTypeDTO;
import com.lds.service.mapper.TransactionTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeMapper transactionTypeMapper;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    /**
     * Save a transactionType.
     *
     * @param transactionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionTypeDTO save(TransactionTypeDTO transactionTypeDTO) {
        LOG.debug("Request to save TransactionType : {}", transactionTypeDTO);
        TransactionType transactionType = transactionTypeMapper.toEntity(transactionTypeDTO);
        transactionType = transactionTypeRepository.save(transactionType);
        return transactionTypeMapper.toDto(transactionType);
    }

    /**
     * Update a transactionType.
     *
     * @param transactionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionTypeDTO update(TransactionTypeDTO transactionTypeDTO) {
        LOG.debug("Request to update TransactionType : {}", transactionTypeDTO);
        TransactionType transactionType = transactionTypeMapper.toEntity(transactionTypeDTO);
        transactionType = transactionTypeRepository.save(transactionType);
        return transactionTypeMapper.toDto(transactionType);
    }

    /**
     * Partially update a transactionType.
     *
     * @param transactionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionTypeDTO> partialUpdate(TransactionTypeDTO transactionTypeDTO) {
        LOG.debug("Request to partially update TransactionType : {}", transactionTypeDTO);

        return transactionTypeRepository
            .findById(transactionTypeDTO.getId())
            .map(existingTransactionType -> {
                transactionTypeMapper.partialUpdate(existingTransactionType, transactionTypeDTO);

                return existingTransactionType;
            })
            .map(transactionTypeRepository::save)
            .map(transactionTypeMapper::toDto);
    }

    /**
     * Get all the transactionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TransactionTypes");
        return transactionTypeRepository.findAll(pageable).map(transactionTypeMapper::toDto);
    }

    /**
     * Get one transactionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionTypeDTO> findOne(Long id) {
        LOG.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id).map(transactionTypeMapper::toDto);
    }

    /**
     * Delete the transactionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }
}
