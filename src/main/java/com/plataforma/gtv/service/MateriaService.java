package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Materia;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Materia}.
 */
public interface MateriaService {
    /**
     * Save a materia.
     *
     * @param materia the entity to save.
     * @return the persisted entity.
     */
    Materia save(Materia materia);

    /**
     * Updates a materia.
     *
     * @param materia the entity to update.
     * @return the persisted entity.
     */
    Materia update(Materia materia);

    /**
     * Partially updates a materia.
     *
     * @param materia the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Materia> partialUpdate(Materia materia);

    /**
     * Get all the materias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Materia> findAll(Pageable pageable);

    /**
     * Get all the materias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Materia> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" materia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Materia> findOne(Long id);

    /**
     * Delete the "id" materia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
