package com.lds.service;

import com.lds.domain.*; // for static metamodels
import com.lds.domain.ReceivedDocument;
import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.criteria.ReceivedDocumentCriteria;
import com.lds.service.dto.ReceivedDocumentDTO;
import com.lds.service.mapper.ReceivedDocumentMapper;
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
 * Service for executing complex queries for {@link ReceivedDocument} entities in the database.
 * The main input is a {@link ReceivedDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReceivedDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReceivedDocumentQueryService extends QueryService<ReceivedDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivedDocumentQueryService.class);

    private final ReceivedDocumentRepository receivedDocumentRepository;

    private final ReceivedDocumentMapper receivedDocumentMapper;

    public ReceivedDocumentQueryService(
        ReceivedDocumentRepository receivedDocumentRepository,
        ReceivedDocumentMapper receivedDocumentMapper
    ) {
        this.receivedDocumentRepository = receivedDocumentRepository;
        this.receivedDocumentMapper = receivedDocumentMapper;
    }

    /**
     * Return a {@link Page} of {@link ReceivedDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReceivedDocumentDTO> findByCriteria(ReceivedDocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReceivedDocument> specification = createSpecification(criteria);
        return receivedDocumentRepository.findAll(specification, page).map(receivedDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReceivedDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReceivedDocument> specification = createSpecification(criteria);
        return receivedDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link ReceivedDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReceivedDocument> createSpecification(ReceivedDocumentCriteria criteria) {
        Specification<ReceivedDocument> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ReceivedDocument_.requestedAction, JoinType.LEFT);
                root.fetch(ReceivedDocument_.typeOfDocument, JoinType.LEFT);
                root.fetch(ReceivedDocument_.office, JoinType.LEFT);
                root.fetch(ReceivedDocument_.responsiblePerson, JoinType.LEFT);
                root.fetch(ReceivedDocument_.documentStatus, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ReceivedDocument_.id),
                    buildRangeSpecification(criteria.getDate(), ReceivedDocument_.date),
                    buildStringSpecification(criteria.getDocumentTitle(), ReceivedDocument_.documentTitle),
                    buildSpecification(criteria.getTransactionType(), ReceivedDocument_.transactionType),
                    buildRangeSpecification(criteria.getDays(), ReceivedDocument_.days),
                    buildRangeSpecification(criteria.getDueDate(), ReceivedDocument_.dueDate),
                    buildRangeSpecification(criteria.getDaysBeforeDue(), ReceivedDocument_.daysBeforeDue),
                    buildRangeSpecification(criteria.getDateReleased(), ReceivedDocument_.dateReleased),
                    buildStringSpecification(criteria.getRemarks(), ReceivedDocument_.remarks),
                    buildSpecification(criteria.getRequestedActionId(), root ->
                        root.join(ReceivedDocument_.requestedAction, JoinType.LEFT).get(RequestedAction_.id)
                    ),
                    buildSpecification(criteria.getTypeOfDocumentId(), root ->
                        root.join(ReceivedDocument_.typeOfDocument, JoinType.LEFT).get(TypeOfDocument_.id)
                    ),
                    buildSpecification(criteria.getOfficeId(), root -> root.join(ReceivedDocument_.office, JoinType.LEFT).get(Office_.id)),
                    buildSpecification(criteria.getResponsiblePersonId(), root ->
                        root.join(ReceivedDocument_.responsiblePerson, JoinType.LEFT).get(ResponsiblePerson_.id)
                    ),
                    buildSpecification(criteria.getDocumentStatusId(), root ->
                        root.join(ReceivedDocument_.documentStatus, JoinType.LEFT).get(DocumentStatus_.id)
                    )
                )
            );
        }
        return specification;
    }
}
