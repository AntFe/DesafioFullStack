package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Aluno;
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
public class AlunoRepositoryWithBagRelationshipsImpl implements AlunoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALUNOS_PARAMETER = "alunos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Aluno> fetchBagRelationships(Optional<Aluno> aluno) {
        return aluno.map(this::fetchCursos);
    }

    @Override
    public Page<Aluno> fetchBagRelationships(Page<Aluno> alunos) {
        return new PageImpl<>(fetchBagRelationships(alunos.getContent()), alunos.getPageable(), alunos.getTotalElements());
    }

    @Override
    public List<Aluno> fetchBagRelationships(List<Aluno> alunos) {
        return Optional.of(alunos).map(this::fetchCursos).orElse(Collections.emptyList());
    }

    Aluno fetchCursos(Aluno result) {
        return entityManager
            .createQuery("select aluno from Aluno aluno left join fetch aluno.cursos where aluno.id = :id", Aluno.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Aluno> fetchCursos(List<Aluno> alunos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alunos.size()).forEach(index -> order.put(alunos.get(index).getId(), index));
        List<Aluno> result = entityManager
            .createQuery("select aluno from Aluno aluno left join fetch aluno.cursos where aluno in :alunos", Aluno.class)
            .setParameter(ALUNOS_PARAMETER, alunos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
