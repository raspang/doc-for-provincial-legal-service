package com.lds.repository;

import com.lds.domain.RequestedAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RequestedAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestedActionRepository extends JpaRepository<RequestedAction, Long> {}
