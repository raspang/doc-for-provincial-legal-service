package com.lds.repository;

import com.lds.domain.DocumentReferenceSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentReferenceSequenceRepository extends JpaRepository<DocumentReferenceSequence, String> {
    /**
     * Atomically increments the sequence.
     * The database handles the locking, preventing race conditions.
     */
    @Modifying
    @Transactional
    @Query("UPDATE DocumentReferenceSequence s SET s.currentSequence = s.currentSequence + 1 WHERE s.yearMonth = :yearMonth")
    int incrementSequence(@Param("yearMonth") String yearMonth);
}
