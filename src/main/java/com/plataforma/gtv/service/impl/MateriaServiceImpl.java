package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Materia;
import com.plataforma.gtv.repository.MateriaRepository;
import com.plataforma.gtv.service.MateriaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Materia}.
 */
@Service
@Transactional
public class MateriaServiceImpl implements MateriaService {

    private static final Logger LOG = LoggerFactory.getLogger(MateriaServiceImpl.class);

    private final MateriaRepository materiaRepository;

    public MateriaServiceImpl(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Override
    public Materia save(Materia materia) {
        LOG.debug("Request to save Materia : {}", materia);
        return materiaRepository.save(materia);
    }

    @Override
    public Materia update(Materia materia) {
        LOG.debug("Request to update Materia : {}", materia);
        return materiaRepository.save(materia);
    }

    @Override
    public Optional<Materia> partialUpdate(Materia materia) {
        LOG.debug("Request to partially update Materia : {}", materia);

        return materiaRepository
            .findById(materia.getId())
            .map(existingMateria -> {
                if (materia.getNomeDaMateria() != null) {
                    existingMateria.setNomeDaMateria(materia.getNomeDaMateria());
                }
                if (materia.getEmenta() != null) {
                    existingMateria.setEmenta(materia.getEmenta());
                }
                if (materia.getReferenciasBibliograficas() != null) {
                    existingMateria.setReferenciasBibliograficas(materia.getReferenciasBibliograficas());
                }

                return existingMateria;
            })
            .map(materiaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Materia> findAll(Pageable pageable) {
        LOG.debug("Request to get all Materias");
        return materiaRepository.findAll(pageable);
    }

    public Page<Materia> findAllWithEagerRelationships(Pageable pageable) {
        return materiaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Materia> findOne(Long id) {
        LOG.debug("Request to get Materia : {}", id);
        return materiaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Materia : {}", id);
        materiaRepository.deleteById(id);
    }
}
