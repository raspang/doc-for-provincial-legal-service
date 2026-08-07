package com.lds.service;

import com.lds.domain.ReceivedDocument;
import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.dto.ReceivedDocumentDTO;
import com.lds.service.mapper.ReceivedDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.ReceivedDocument}.
 */
@Service
@Transactional
public class ReceivedDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivedDocumentService.class);

    private final ReceivedDocumentRepository receivedDocumentRepository;

    private final ReceivedDocumentMapper receivedDocumentMapper;

    public ReceivedDocumentService(ReceivedDocumentRepository receivedDocumentRepository, ReceivedDocumentMapper receivedDocumentMapper) {
        this.receivedDocumentRepository = receivedDocumentRepository;
        this.receivedDocumentMapper = receivedDocumentMapper;
    }

    /**
     * Save a receivedDocument.
     *
     * @param receivedDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceivedDocumentDTO save(ReceivedDocumentDTO receivedDocumentDTO) {
        LOG.debug("Request to save ReceivedDocument : {}", receivedDocumentDTO);
        ReceivedDocument receivedDocument = receivedDocumentMapper.toEntity(receivedDocumentDTO);
        receivedDocument = receivedDocumentRepository.save(receivedDocument);
        return receivedDocumentMapper.toDto(receivedDocument);
    }

    /**
     * Update a receivedDocument.
     *
     * @param receivedDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceivedDocumentDTO update(ReceivedDocumentDTO receivedDocumentDTO) {
        LOG.debug("Request to update ReceivedDocument : {}", receivedDocumentDTO);
        ReceivedDocument receivedDocument = receivedDocumentMapper.toEntity(receivedDocumentDTO);
        receivedDocument = receivedDocumentRepository.save(receivedDocument);
        return receivedDocumentMapper.toDto(receivedDocument);
    }

    /**
     * Partially update a receivedDocument.
     *
     * @param receivedDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReceivedDocumentDTO> partialUpdate(ReceivedDocumentDTO receivedDocumentDTO) {
        LOG.debug("Request to partially update ReceivedDocument : {}", receivedDocumentDTO);

        return receivedDocumentRepository
            .findById(receivedDocumentDTO.getId())
            .map(existingReceivedDocument -> {
                receivedDocumentMapper.partialUpdate(existingReceivedDocument, receivedDocumentDTO);

                return existingReceivedDocument;
            })
            .map(receivedDocumentRepository::save)
            .map(receivedDocumentMapper::toDto);
    }

    /**
     * Get all the receivedDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReceivedDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return receivedDocumentRepository.findAllWithEagerRelationships(pageable).map(receivedDocumentMapper::toDto);
    }

    /**
     * Get one receivedDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReceivedDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get ReceivedDocument : {}", id);
        return receivedDocumentRepository.findOneWithEagerRelationships(id).map(receivedDocumentMapper::toDto);
    }

    /**
     * Delete the receivedDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReceivedDocument : {}", id);
        receivedDocumentRepository.deleteById(id);
    }
}
