package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Professor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Professor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {}
