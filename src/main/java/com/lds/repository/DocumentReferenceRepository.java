package com.lds.repository;

import com.lds.domain.DocumentReference;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentReference entity.
 */
@Repository
public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, Long>, JpaSpecificationExecutor<DocumentReference> {
    default Optional<DocumentReference> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocumentReference> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocumentReference> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select documentReference from DocumentReference documentReference left join fetch documentReference.typeOfDocument",
        countQuery = "select count(documentReference) from DocumentReference documentReference"
    )
    Page<DocumentReference> findAllWithToOneRelationships(Pageable pageable);

    @Query("select documentReference from DocumentReference documentReference left join fetch documentReference.typeOfDocument")
    List<DocumentReference> findAllWithToOneRelationships();

    @Query(
        "select documentReference from DocumentReference documentReference left join fetch documentReference.typeOfDocument where documentReference.id =:id"
    )
    Optional<DocumentReference> findOneWithToOneRelationships(@Param("id") Long id);
}
