package com.plataforma.gtv.web.rest;

import com.plataforma.gtv.domain.Materia;
import com.plataforma.gtv.repository.MateriaRepository;
import com.plataforma.gtv.service.MateriaService;
import com.plataforma.gtv.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.plataforma.gtv.domain.Materia}.
 */
@RestController
@RequestMapping("/api/materias")
public class MateriaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MateriaResource.class);

    private static final String ENTITY_NAME = "materia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MateriaService materiaService;

    private final MateriaRepository materiaRepository;

    public MateriaResource(MateriaService materiaService, MateriaRepository materiaRepository) {
        this.materiaService = materiaService;
        this.materiaRepository = materiaRepository;
    }

    /**
     * {@code POST  /materias} : Create a new materia.
     *
     * @param materia the materia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materia, or with status {@code 400 (Bad Request)} if the materia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Materia> createMateria(@RequestBody Materia materia) throws URISyntaxException {
        LOG.debug("REST request to save Materia : {}", materia);
        if (materia.getId() != null) {
            throw new BadRequestAlertException("A new materia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materia = materiaService.save(materia);
        return ResponseEntity.created(new URI("/api/materias/" + materia.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, materia.getId().toString()))
            .body(materia);
    }

    /**
     * {@code PUT  /materias/:id} : Updates an existing materia.
     *
     * @param id the id of the materia to save.
     * @param materia the materia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materia,
     * or with status {@code 400 (Bad Request)} if the materia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Materia> updateMateria(@PathVariable(value = "id", required = false) final Long id, @RequestBody Materia materia)
        throws URISyntaxException {
        LOG.debug("REST request to update Materia : {}, {}", id, materia);
        if (materia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materia = materiaService.update(materia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materia.getId().toString()))
            .body(materia);
    }

    /**
     * {@code PATCH  /materias/:id} : Partial updates given fields of an existing materia, field will ignore if it is null
     *
     * @param id the id of the materia to save.
     * @param materia the materia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materia,
     * or with status {@code 400 (Bad Request)} if the materia is not valid,
     * or with status {@code 404 (Not Found)} if the materia is not found,
     * or with status {@code 500 (Internal Server Error)} if the materia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Materia> partialUpdateMateria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Materia materia
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Materia partially : {}, {}", id, materia);
        if (materia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Materia> result = materiaService.partialUpdate(materia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materia.getId().toString())
        );
    }

    /**
     * {@code GET  /materias} : get all the materias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Materia>> getAllMaterias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Materias");
        Page<Materia> page;
        if (eagerload) {
            page = materiaService.findAllWithEagerRelationships(pageable);
        } else {
            page = materiaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materias/:id} : get the "id" materia.
     *
     * @param id the id of the materia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Materia> getMateria(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Materia : {}", id);
        Optional<Materia> materia = materiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materia);
    }

    /**
     * {@code DELETE  /materias/:id} : delete the "id" materia.
     *
     * @param id the id of the materia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateria(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Materia : {}", id);
        materiaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
