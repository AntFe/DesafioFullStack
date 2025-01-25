package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Curso;
import com.plataforma.gtv.repository.CursoRepository;
import com.plataforma.gtv.service.CursoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Curso}.
 */
@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private static final Logger LOG = LoggerFactory.getLogger(CursoServiceImpl.class);

    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public Curso save(Curso curso) {
        LOG.debug("Request to save Curso : {}", curso);
        return cursoRepository.save(curso);
    }

    @Override
    public Curso update(Curso curso) {
        LOG.debug("Request to update Curso : {}", curso);
        return cursoRepository.save(curso);
    }

    @Override
    public Optional<Curso> partialUpdate(Curso curso) {
        LOG.debug("Request to partially update Curso : {}", curso);

        return cursoRepository
            .findById(curso.getId())
            .map(existingCurso -> {
                if (curso.getNome() != null) {
                    existingCurso.setNome(curso.getNome());
                }

                return existingCurso;
            })
            .map(cursoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Curso> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cursos");
        return cursoRepository.findAll(pageable);
    }

    public Page<Curso> findAllWithEagerRelationships(Pageable pageable) {
        return cursoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findOne(Long id) {
        LOG.debug("Request to get Curso : {}", id);
        return cursoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Curso : {}", id);
        cursoRepository.deleteById(id);
    }
}
