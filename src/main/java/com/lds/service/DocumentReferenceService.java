package com.lds.service;

import com.lds.domain.DocumentReference;
import com.lds.domain.DocumentReferenceSequence;
import com.lds.repository.DocumentReferenceRepository;
import com.lds.repository.DocumentReferenceSequenceRepository;
import com.lds.service.dto.DocumentReferenceDTO;
import com.lds.service.mapper.DocumentReferenceMapper;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.DocumentReference}.
 */
@Service
@Transactional
public class DocumentReferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentReferenceService.class);

    private static final String PREFIX = "PLSO";

    private final DocumentReferenceRepository documentReferenceRepository;

    private final DocumentReferenceMapper documentReferenceMapper;

    private final DocumentReferenceSequenceRepository sequenceRepository;

    public DocumentReferenceService(
        DocumentReferenceRepository documentReferenceRepository,
        DocumentReferenceMapper documentReferenceMapper,
        DocumentReferenceSequenceRepository sequenceRepository
    ) {
        this.documentReferenceRepository = documentReferenceRepository;
        this.documentReferenceMapper = documentReferenceMapper;
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public String generateNextReferenceNumber() {
        String yearMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // Loop to handle the rare race condition of a brand new month
        while (true) {
            try {
                // 1. Attempt to increment atomically
                int rowsUpdated = sequenceRepository.incrementSequence(yearMonth);

                if (rowsUpdated > 0) {
                    // Successfully incremented an existing sequence
                    DocumentReferenceSequence sequence = sequenceRepository
                        .findById(yearMonth)
                        .orElseThrow(() -> new RuntimeException("Sequence not found after increment"));

                    return String.format("%s-%s-%03d", PREFIX, yearMonth, sequence.getCurrentSequence());
                } else {
                    // 2. If 0 rows updated, it's a new month. Create it starting at 1.
                    DocumentReferenceSequence newSequence = new DocumentReferenceSequence();
                    newSequence.setYearMonth(yearMonth);
                    newSequence.setCurrentSequence(1L);
                    sequenceRepository.save(newSequence);

                    return String.format("%s-%s-%03d", PREFIX, yearMonth, 1L);
                }
            } catch (DataIntegrityViolationException e) {
                // 3. Edge case: Another thread just created the sequence for this new month
                // before we could save ours. The loop will restart, and the increment
                // will now succeed (rowsUpdated > 0).
                continue;
            }
        }
    }

    /**
     * Save a documentReference.
     *
     * @param documentReferenceDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentReferenceDTO save(DocumentReferenceDTO documentReferenceDTO) {
        LOG.debug("Request to save DocumentReference : {}", documentReferenceDTO);

        String refNumber = generateNextReferenceNumber();
        documentReferenceDTO.setReferenceNo(refNumber);

        DocumentReference documentReference = documentReferenceMapper.toEntity(documentReferenceDTO);
        documentReference = documentReferenceRepository.save(documentReference);
        return documentReferenceMapper.toDto(documentReference);
    }

    /**
     * Update a documentReference.
     *
     * @param documentReferenceDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentReferenceDTO update(DocumentReferenceDTO documentReferenceDTO) {
        LOG.debug("Request to update DocumentReference : {}", documentReferenceDTO);
        DocumentReference documentReference = documentReferenceMapper.toEntity(documentReferenceDTO);
        documentReference = documentReferenceRepository.save(documentReference);
        return documentReferenceMapper.toDto(documentReference);
    }

    /**
     * Partially update a documentReference.
     *
     * @param documentReferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentReferenceDTO> partialUpdate(DocumentReferenceDTO documentReferenceDTO) {
        LOG.debug("Request to partially update DocumentReference : {}", documentReferenceDTO);

        return documentReferenceRepository
            .findById(documentReferenceDTO.getId())
            .map(existingDocumentReference -> {
                documentReferenceMapper.partialUpdate(existingDocumentReference, documentReferenceDTO);

                return existingDocumentReference;
            })
            .map(documentReferenceRepository::save)
            .map(documentReferenceMapper::toDto);
    }

    /**
     * Get all the documentReferences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DocumentReferenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return documentReferenceRepository.findAllWithEagerRelationships(pageable).map(documentReferenceMapper::toDto);
    }

    /**
     * Get one documentReference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentReferenceDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentReference : {}", id);
        return documentReferenceRepository.findOneWithEagerRelationships(id).map(documentReferenceMapper::toDto);
    }

    /**
     * Delete the documentReference by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentReference : {}", id);
        documentReferenceRepository.deleteById(id);
    }
}
