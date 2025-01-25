package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CursoRepositoryWithBagRelationshipsImpl implements CursoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CURSOS_PARAMETER = "cursos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Curso> fetchBagRelationships(Optional<Curso> curso) {
        return curso.map(this::fetchMaterias);
    }

    @Override
    public Page<Curso> fetchBagRelationships(Page<Curso> cursos) {
        return new PageImpl<>(fetchBagRelationships(cursos.getContent()), cursos.getPageable(), cursos.getTotalElements());
    }

    @Override
    public List<Curso> fetchBagRelationships(List<Curso> cursos) {
        return Optional.of(cursos).map(this::fetchMaterias).orElse(Collections.emptyList());
    }

    Curso fetchMaterias(Curso result) {
        return entityManager
            .createQuery("select curso from Curso curso left join fetch curso.materias where curso.id = :id", Curso.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Curso> fetchMaterias(List<Curso> cursos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cursos.size()).forEach(index -> order.put(cursos.get(index).getId(), index));
        List<Curso> result = entityManager
            .createQuery("select curso from Curso curso left join fetch curso.materias where curso in :cursos", Curso.class)
            .setParameter(CURSOS_PARAMETER, cursos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
