package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Materia;
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
public class MateriaRepositoryWithBagRelationshipsImpl implements MateriaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MATERIAS_PARAMETER = "materias";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Materia> fetchBagRelationships(Optional<Materia> materia) {
        return materia.map(this::fetchAulas);
    }

    @Override
    public Page<Materia> fetchBagRelationships(Page<Materia> materias) {
        return new PageImpl<>(fetchBagRelationships(materias.getContent()), materias.getPageable(), materias.getTotalElements());
    }

    @Override
    public List<Materia> fetchBagRelationships(List<Materia> materias) {
        return Optional.of(materias).map(this::fetchAulas).orElse(Collections.emptyList());
    }

    Materia fetchAulas(Materia result) {
        return entityManager
            .createQuery("select materia from Materia materia left join fetch materia.aulas where materia.id = :id", Materia.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Materia> fetchAulas(List<Materia> materias) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, materias.size()).forEach(index -> order.put(materias.get(index).getId(), index));
        List<Materia> result = entityManager
            .createQuery("select materia from Materia materia left join fetch materia.aulas where materia in :materias", Materia.class)
            .setParameter(MATERIAS_PARAMETER, materias)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
