package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Pais;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Pais}.
 */
public interface PaisService {
    /**
     * Save a pais.
     *
     * @param pais the entity to save.
     * @return the persisted entity.
     */
    Pais save(Pais pais);

    /**
     * Updates a pais.
     *
     * @param pais the entity to update.
     * @return the persisted entity.
     */
    Pais update(Pais pais);

    /**
     * Partially updates a pais.
     *
     * @param pais the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pais> partialUpdate(Pais pais);

    /**
     * Get all the pais.
     *
     * @return the list of entities.
     */
    List<Pais> findAll();

    /**
     * Get all the Pais where Endereco is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Pais> findAllWhereEnderecoIsNull();

    /**
     * Get all the pais with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pais> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pais> findOne(Long id);

    /**
     * Delete the "id" pais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
