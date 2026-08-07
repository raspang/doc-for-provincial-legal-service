package com.lds.repository;

import com.lds.domain.DocumentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, Long> {}
