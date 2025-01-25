package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Curso;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Curso}.
 */
public interface CursoService {
    /**
     * Save a curso.
     *
     * @param curso the entity to save.
     * @return the persisted entity.
     */
    Curso save(Curso curso);

    /**
     * Updates a curso.
     *
     * @param curso the entity to update.
     * @return the persisted entity.
     */
    Curso update(Curso curso);

    /**
     * Partially updates a curso.
     *
     * @param curso the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Curso> partialUpdate(Curso curso);

    /**
     * Get all the cursos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Curso> findAll(Pageable pageable);

    /**
     * Get all the cursos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Curso> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" curso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Curso> findOne(Long id);

    /**
     * Delete the "id" curso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
