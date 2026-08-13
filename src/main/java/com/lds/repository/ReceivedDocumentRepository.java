package com.lds.repository;

import com.lds.domain.ReceivedDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReceivedDocument entity.
 */
@Repository
public interface ReceivedDocumentRepository extends JpaRepository<ReceivedDocument, Long>, JpaSpecificationExecutor<ReceivedDocument> {
    default Optional<ReceivedDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReceivedDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReceivedDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select receivedDocument from ReceivedDocument receivedDocument left join fetch receivedDocument.requestedAction left join fetch receivedDocument.typeOfDocument left join fetch receivedDocument.office left join fetch receivedDocument.responsiblePerson left join fetch receivedDocument.documentStatus left join fetch receivedDocument.transactionType",
        countQuery = "select count(receivedDocument) from ReceivedDocument receivedDocument"
    )
    Page<ReceivedDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select receivedDocument from ReceivedDocument receivedDocument left join fetch receivedDocument.requestedAction left join fetch receivedDocument.typeOfDocument left join fetch receivedDocument.office left join fetch receivedDocument.responsiblePerson left join fetch receivedDocument.documentStatus left join fetch receivedDocument.transactionType"
    )
    List<ReceivedDocument> findAllWithToOneRelationships();

    @Query(
        "select receivedDocument from ReceivedDocument receivedDocument left join fetch receivedDocument.requestedAction left join fetch receivedDocument.typeOfDocument left join fetch receivedDocument.office left join fetch receivedDocument.responsiblePerson left join fetch receivedDocument.documentStatus left join fetch receivedDocument.transactionType where receivedDocument.id =:id"
    )
    Optional<ReceivedDocument> findOneWithToOneRelationships(@Param("id") Long id);

    /**
     * Find all received documents where:
     * - documentStatus.warning = true
     * - responsiblePerson is not null (needed for email)
     * - transactionType is not null (needed for targetDays)
     *
     * The actual dueDate computation and daysBeforeDue filtering is done in the service layer
     * because it requires date arithmetic: dueDate = date + targetDays.
     */
    @Query(
        """
            SELECT rd FROM ReceivedDocument rd
            LEFT JOIN FETCH rd.transactionType tt
            LEFT JOIN FETCH rd.documentStatus ds
            LEFT JOIN FETCH rd.responsiblePerson rp
            LEFT JOIN FETCH rd.typeOfDocument
            LEFT JOIN FETCH rd.office
            LEFT JOIN FETCH rd.requestedAction
            WHERE ds.warning = true
              AND rp IS NOT NULL
              AND tt IS NOT NULL
        """
    )
    List<ReceivedDocument> findAllWithWarningAndResponsiblePerson();
}
