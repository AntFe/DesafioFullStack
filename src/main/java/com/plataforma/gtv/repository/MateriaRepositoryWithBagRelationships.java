package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Materia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MateriaRepositoryWithBagRelationships {
    Optional<Materia> fetchBagRelationships(Optional<Materia> materia);

    List<Materia> fetchBagRelationships(List<Materia> materias);

    Page<Materia> fetchBagRelationships(Page<Materia> materias);
}
