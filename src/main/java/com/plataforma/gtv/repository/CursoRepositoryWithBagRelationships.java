package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Curso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CursoRepositoryWithBagRelationships {
    Optional<Curso> fetchBagRelationships(Optional<Curso> curso);

    List<Curso> fetchBagRelationships(List<Curso> cursos);

    Page<Curso> fetchBagRelationships(Page<Curso> cursos);
}
