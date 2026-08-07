package com.lds.repository;

import com.lds.domain.TypeOfDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeOfDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOfDocumentRepository extends JpaRepository<TypeOfDocument, Long> {}
