package com.lds.repository;

import com.lds.domain.ReceivedDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
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
}
