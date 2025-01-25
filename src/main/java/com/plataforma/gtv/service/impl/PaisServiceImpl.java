package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Pais;
import com.plataforma.gtv.repository.PaisRepository;
import com.plataforma.gtv.service.PaisService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Pais}.
 */
@Service
@Transactional
public class PaisServiceImpl implements PaisService {

    private static final Logger LOG = LoggerFactory.getLogger(PaisServiceImpl.class);

    private final PaisRepository paisRepository;

    public PaisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public Pais save(Pais pais) {
        LOG.debug("Request to save Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Pais update(Pais pais) {
        LOG.debug("Request to update Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Optional<Pais> partialUpdate(Pais pais) {
        LOG.debug("Request to partially update Pais : {}", pais);

        return paisRepository
            .findById(pais.getId())
            .map(existingPais -> {
                if (pais.getNomeDoPais() != null) {
                    existingPais.setNomeDoPais(pais.getNomeDoPais());
                }

                return existingPais;
            })
            .map(paisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pais> findAll() {
        LOG.debug("Request to get all Pais");
        return paisRepository.findAll();
    }

    public Page<Pais> findAllWithEagerRelationships(Pageable pageable) {
        return paisRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the pais where Endereco is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pais> findAllWhereEnderecoIsNull() {
        LOG.debug("Request to get all pais where Endereco is null");
        return StreamSupport.stream(paisRepository.findAll().spliterator(), false).filter(pais -> pais.getEndereco() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> findOne(Long id) {
        LOG.debug("Request to get Pais : {}", id);
        return paisRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
    }
}
