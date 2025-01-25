package com.plataforma.gtv.service.impl;

import com.plataforma.gtv.domain.Local;
import com.plataforma.gtv.repository.LocalRepository;
import com.plataforma.gtv.service.LocalService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.plataforma.gtv.domain.Local}.
 */
@Service
@Transactional
public class LocalServiceImpl implements LocalService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalServiceImpl.class);

    private final LocalRepository localRepository;

    public LocalServiceImpl(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    @Override
    public Local save(Local local) {
        LOG.debug("Request to save Local : {}", local);
        return localRepository.save(local);
    }

    @Override
    public Local update(Local local) {
        LOG.debug("Request to update Local : {}", local);
        return localRepository.save(local);
    }

    @Override
    public Optional<Local> partialUpdate(Local local) {
        LOG.debug("Request to partially update Local : {}", local);

        return localRepository
            .findById(local.getId())
            .map(existingLocal -> {
                if (local.getNomeDoLocal() != null) {
                    existingLocal.setNomeDoLocal(local.getNomeDoLocal());
                }

                return existingLocal;
            })
            .map(localRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Local> findAll() {
        LOG.debug("Request to get all Locals");
        return localRepository.findAll();
    }

    /**
     *  Get all the locals where Pais is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Local> findAllWherePaisIsNull() {
        LOG.debug("Request to get all locals where Pais is null");
        return StreamSupport.stream(localRepository.findAll().spliterator(), false).filter(local -> local.getPais() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Local> findOne(Long id) {
        LOG.debug("Request to get Local : {}", id);
        return localRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Local : {}", id);
        localRepository.deleteById(id);
    }
}
