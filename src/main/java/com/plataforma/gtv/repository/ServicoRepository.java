package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Servico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Servico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {}
