package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Local;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Local}.
 */
public interface LocalService {
    /**
     * Save a local.
     *
     * @param local the entity to save.
     * @return the persisted entity.
     */
    Local save(Local local);

    /**
     * Updates a local.
     *
     * @param local the entity to update.
     * @return the persisted entity.
     */
    Local update(Local local);

    /**
     * Partially updates a local.
     *
     * @param local the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Local> partialUpdate(Local local);

    /**
     * Get all the locals.
     *
     * @return the list of entities.
     */
    List<Local> findAll();

    /**
     * Get all the Local where Pais is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Local> findAllWherePaisIsNull();

    /**
     * Get the "id" local.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Local> findOne(Long id);

    /**
     * Delete the "id" local.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
