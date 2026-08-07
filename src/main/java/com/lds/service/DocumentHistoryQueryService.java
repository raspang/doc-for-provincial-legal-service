package com.lds.service;

import com.lds.domain.*; // for static metamodels
import com.lds.domain.DocumentHistory;
import com.lds.repository.DocumentHistoryRepository;
import com.lds.service.criteria.DocumentHistoryCriteria;
import com.lds.service.dto.DocumentHistoryDTO;
import com.lds.service.mapper.DocumentHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentHistory} entities in the database.
 * The main input is a {@link DocumentHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentHistoryQueryService extends QueryService<DocumentHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHistoryQueryService.class);

    private final DocumentHistoryRepository documentHistoryRepository;

    private final DocumentHistoryMapper documentHistoryMapper;

    public DocumentHistoryQueryService(DocumentHistoryRepository documentHistoryRepository, DocumentHistoryMapper documentHistoryMapper) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.documentHistoryMapper = documentHistoryMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentHistoryDTO> findByCriteria(DocumentHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentHistory> specification = createSpecification(criteria);
        return documentHistoryRepository.findAll(specification, page).map(documentHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentHistory> specification = createSpecification(criteria);
        return documentHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentHistory> createSpecification(DocumentHistoryCriteria criteria) {
        Specification<DocumentHistory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), DocumentHistory_.id),
                    buildRangeSpecification(criteria.getDocumentId(), DocumentHistory_.documentId),
                    buildSpecification(criteria.getDocumentType(), DocumentHistory_.documentType),
                    buildStringSpecification(criteria.getAction(), DocumentHistory_.action),
                    buildStringSpecification(criteria.getChangedBy(), DocumentHistory_.changedBy),
                    buildRangeSpecification(criteria.getTimestamp(), DocumentHistory_.timestamp),
                    buildStringSpecification(criteria.getPreviousValue(), DocumentHistory_.previousValue),
                    buildStringSpecification(criteria.getNewValue(), DocumentHistory_.newValue)
                )
            );
        }
        return specification;
    }
}
