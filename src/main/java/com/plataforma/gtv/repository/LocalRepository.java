package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Local;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Local entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {}
