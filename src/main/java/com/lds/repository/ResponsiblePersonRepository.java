package com.lds.repository;

import com.lds.domain.ResponsiblePerson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResponsiblePerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, Long> {}
