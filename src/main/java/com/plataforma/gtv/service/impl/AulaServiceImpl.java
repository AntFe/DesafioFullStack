package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Aula;
import com.plataforma.gtv.repository.AulaRepository;
import com.plataforma.gtv.service.AulaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Aula}.
 */
@Service
@Transactional
public class AulaServiceImpl implements AulaService {

    private static final Logger LOG = LoggerFactory.getLogger(AulaServiceImpl.class);

    private final AulaRepository aulaRepository;

    public AulaServiceImpl(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @Override
    public Aula save(Aula aula) {
        LOG.debug("Request to save Aula : {}", aula);
        return aulaRepository.save(aula);
    }

    @Override
    public Aula update(Aula aula) {
        LOG.debug("Request to update Aula : {}", aula);
        return aulaRepository.save(aula);
    }

    @Override
    public Optional<Aula> partialUpdate(Aula aula) {
        LOG.debug("Request to partially update Aula : {}", aula);

        return aulaRepository
            .findById(aula.getId())
            .map(existingAula -> {
                if (aula.getTituloAula() != null) {
                    existingAula.setTituloAula(aula.getTituloAula());
                }
                if (aula.getDescricao() != null) {
                    existingAula.setDescricao(aula.getDescricao());
                }
                if (aula.getLinkVideo() != null) {
                    existingAula.setLinkVideo(aula.getLinkVideo());
                }
                if (aula.getLinkArquivos() != null) {
                    existingAula.setLinkArquivos(aula.getLinkArquivos());
                }
                if (aula.getResumo() != null) {
                    existingAula.setResumo(aula.getResumo());
                }

                return existingAula;
            })
            .map(aulaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aula> findAll() {
        LOG.debug("Request to get all Aulas");
        return aulaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aula> findOne(Long id) {
        LOG.debug("Request to get Aula : {}", id);
        return aulaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Aula : {}", id);
        aulaRepository.deleteById(id);
    }
}
