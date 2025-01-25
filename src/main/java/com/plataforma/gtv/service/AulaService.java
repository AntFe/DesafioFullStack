package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Aula;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Aula}.
 */
public interface AulaService {
    /**
     * Save a aula.
     *
     * @param aula the entity to save.
     * @return the persisted entity.
     */
    Aula save(Aula aula);

    /**
     * Updates a aula.
     *
     * @param aula the entity to update.
     * @return the persisted entity.
     */
    Aula update(Aula aula);

    /**
     * Partially updates a aula.
     *
     * @param aula the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Aula> partialUpdate(Aula aula);

    /**
     * Get all the aulas.
     *
     * @return the list of entities.
     */
    List<Aula> findAll();

    /**
     * Get the "id" aula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Aula> findOne(Long id);

    /**
     * Delete the "id" aula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
