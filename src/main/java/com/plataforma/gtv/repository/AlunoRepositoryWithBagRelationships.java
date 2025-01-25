package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Aluno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlunoRepositoryWithBagRelationships {
    Optional<Aluno> fetchBagRelationships(Optional<Aluno> aluno);

    List<Aluno> fetchBagRelationships(List<Aluno> alunos);

    Page<Aluno> fetchBagRelationships(Page<Aluno> alunos);
}
