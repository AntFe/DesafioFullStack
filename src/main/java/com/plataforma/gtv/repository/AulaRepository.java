package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Aula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {}
