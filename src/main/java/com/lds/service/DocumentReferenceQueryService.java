package com.lds.service;

import com.lds.domain.*; // for static metamodels
import com.lds.domain.DocumentReference;
import com.lds.repository.DocumentReferenceRepository;
import com.lds.service.criteria.DocumentReferenceCriteria;
import com.lds.service.dto.DocumentReferenceDTO;
import com.lds.service.mapper.DocumentReferenceMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocumentReference} entities in the database.
 * The main input is a {@link DocumentReferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentReferenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentReferenceQueryService extends QueryService<DocumentReference> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentReferenceQueryService.class);

    private final DocumentReferenceRepository documentReferenceRepository;

    private final DocumentReferenceMapper documentReferenceMapper;

    public DocumentReferenceQueryService(
        DocumentReferenceRepository documentReferenceRepository,
        DocumentReferenceMapper documentReferenceMapper
    ) {
        this.documentReferenceRepository = documentReferenceRepository;
        this.documentReferenceMapper = documentReferenceMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentReferenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentReferenceDTO> findByCriteria(DocumentReferenceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentReference> specification = createSpecification(criteria);
        return documentReferenceRepository.findAll(specification, page).map(documentReferenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentReferenceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentReference> specification = createSpecification(criteria);
        return documentReferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentReferenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentReference> createSpecification(DocumentReferenceCriteria criteria) {
        Specification<DocumentReference> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(DocumentReference_.typeOfDocument, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), DocumentReference_.id),
                    buildRangeSpecification(criteria.getDate(), DocumentReference_.date),
                    buildStringSpecification(criteria.getReferenceNo(), DocumentReference_.referenceNo),
                    buildStringSpecification(criteria.getDocumentTitle(), DocumentReference_.documentTitle),
                    buildStringSpecification(criteria.getAuthor(), DocumentReference_.author),
                    buildRangeSpecification(criteria.getDateReleased(), DocumentReference_.dateReleased),
                    buildRangeSpecification(criteria.getSubmittedToSirKing(), DocumentReference_.submittedToSirKing),
                    buildStringSpecification(criteria.getRemarks(), DocumentReference_.remarks),
                    buildSpecification(criteria.getTypeOfDocumentId(), root ->
                        root.join(DocumentReference_.typeOfDocument, JoinType.LEFT).get(TypeOfDocument_.id)
                    )
                )
            );
        }
        return specification;
    }
}
