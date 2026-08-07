package com.lds.service;

import com.lds.domain.DocumentHistory;
import com.lds.repository.DocumentHistoryRepository;
import com.lds.service.dto.DocumentHistoryDTO;
import com.lds.service.mapper.DocumentHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.DocumentHistory}.
 */
@Service
@Transactional
public class DocumentHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHistoryService.class);

    private final DocumentHistoryRepository documentHistoryRepository;

    private final DocumentHistoryMapper documentHistoryMapper;

    public DocumentHistoryService(DocumentHistoryRepository documentHistoryRepository, DocumentHistoryMapper documentHistoryMapper) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.documentHistoryMapper = documentHistoryMapper;
    }

    /**
     * Save a documentHistory.
     *
     * @param documentHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentHistoryDTO save(DocumentHistoryDTO documentHistoryDTO) {
        LOG.debug("Request to save DocumentHistory : {}", documentHistoryDTO);
        DocumentHistory documentHistory = documentHistoryMapper.toEntity(documentHistoryDTO);
        documentHistory = documentHistoryRepository.save(documentHistory);
        return documentHistoryMapper.toDto(documentHistory);
    }

    /**
     * Update a documentHistory.
     *
     * @param documentHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentHistoryDTO update(DocumentHistoryDTO documentHistoryDTO) {
        LOG.debug("Request to update DocumentHistory : {}", documentHistoryDTO);
        DocumentHistory documentHistory = documentHistoryMapper.toEntity(documentHistoryDTO);
        documentHistory = documentHistoryRepository.save(documentHistory);
        return documentHistoryMapper.toDto(documentHistory);
    }

    /**
     * Partially update a documentHistory.
     *
     * @param documentHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentHistoryDTO> partialUpdate(DocumentHistoryDTO documentHistoryDTO) {
        LOG.debug("Request to partially update DocumentHistory : {}", documentHistoryDTO);

        return documentHistoryRepository
            .findById(documentHistoryDTO.getId())
            .map(existingDocumentHistory -> {
                documentHistoryMapper.partialUpdate(existingDocumentHistory, documentHistoryDTO);

                return existingDocumentHistory;
            })
            .map(documentHistoryRepository::save)
            .map(documentHistoryMapper::toDto);
    }

    /**
     * Get one documentHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentHistory : {}", id);
        return documentHistoryRepository.findById(id).map(documentHistoryMapper::toDto);
    }

    /**
     * Delete the documentHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentHistory : {}", id);
        documentHistoryRepository.deleteById(id);
    }
}
