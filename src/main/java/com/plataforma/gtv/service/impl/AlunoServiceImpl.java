package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Aluno;
import com.plataforma.gtv.repository.AlunoRepository;
import com.plataforma.gtv.service.AlunoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Aluno}.
 */
@Service
@Transactional
public class AlunoServiceImpl implements AlunoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlunoServiceImpl.class);

    private final AlunoRepository alunoRepository;

    public AlunoServiceImpl(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public Aluno save(Aluno aluno) {
        LOG.debug("Request to save Aluno : {}", aluno);
        return alunoRepository.save(aluno);
    }

    @Override
    public Aluno update(Aluno aluno) {
        LOG.debug("Request to update Aluno : {}", aluno);
        return alunoRepository.save(aluno);
    }

    @Override
    public Optional<Aluno> partialUpdate(Aluno aluno) {
        LOG.debug("Request to partially update Aluno : {}", aluno);

        return alunoRepository
            .findById(aluno.getId())
            .map(existingAluno -> {
                if (aluno.getNome() != null) {
                    existingAluno.setNome(aluno.getNome());
                }
                if (aluno.getSobrenome() != null) {
                    existingAluno.setSobrenome(aluno.getSobrenome());
                }
                if (aluno.getEmail() != null) {
                    existingAluno.setEmail(aluno.getEmail());
                }
                if (aluno.getNumeroTelefone() != null) {
                    existingAluno.setNumeroTelefone(aluno.getNumeroTelefone());
                }
                if (aluno.getMatriculaData() != null) {
                    existingAluno.setMatriculaData(aluno.getMatriculaData());
                }
                if (aluno.getMaticula() != null) {
                    existingAluno.setMaticula(aluno.getMaticula());
                }

                return existingAluno;
            })
            .map(alunoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Aluno> findAll(Pageable pageable) {
        LOG.debug("Request to get all Alunos");
        return alunoRepository.findAll(pageable);
    }

    public Page<Aluno> findAllWithEagerRelationships(Pageable pageable) {
        return alunoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aluno> findOne(Long id) {
        LOG.debug("Request to get Aluno : {}", id);
        return alunoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Aluno : {}", id);
        alunoRepository.deleteById(id);
    }
}
