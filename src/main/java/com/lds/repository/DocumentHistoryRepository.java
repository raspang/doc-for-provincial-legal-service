package com.lds.repository;

import com.lds.domain.DocumentHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long>, JpaSpecificationExecutor<DocumentHistory> {}
