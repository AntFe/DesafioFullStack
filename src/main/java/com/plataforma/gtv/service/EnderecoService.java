package com.plataforma.gtv.service;

import com.plataforma.gtv.domain.Endereco;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.plataforma.gtv.domain.Endereco}.
 */
public interface EnderecoService {
    /**
     * Save a endereco.
     *
     * @param endereco the entity to save.
     * @return the persisted entity.
     */
    Endereco save(Endereco endereco);

    /**
     * Updates a endereco.
     *
     * @param endereco the entity to update.
     * @return the persisted entity.
     */
    Endereco update(Endereco endereco);

    /**
     * Partially updates a endereco.
     *
     * @param endereco the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Endereco> partialUpdate(Endereco endereco);

    /**
     * Get all the enderecos.
     *
     * @return the list of entities.
     */
    List<Endereco> findAll();

    /**
     * Get all the Endereco where Aluno is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Endereco> findAllWhereAlunoIsNull();

    /**
     * Get the "id" endereco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Endereco> findOne(Long id);

    /**
     * Delete the "id" endereco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
