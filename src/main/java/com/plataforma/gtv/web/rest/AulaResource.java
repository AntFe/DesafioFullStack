package com.plataforma.gtv.web.rest;

import com.plataforma.gtv.domain.Aula;
import com.plataforma.gtv.repository.AulaRepository;
import com.plataforma.gtv.service.AulaService;
import com.plataforma.gtv.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.plataforma.gtv.domain.Aula}.
 */
@RestController
@RequestMapping("/api/aulas")
public class AulaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AulaResource.class);

    private static final String ENTITY_NAME = "aula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AulaService aulaService;

    private final AulaRepository aulaRepository;

    public AulaResource(AulaService aulaService, AulaRepository aulaRepository) {
        this.aulaService = aulaService;
        this.aulaRepository = aulaRepository;
    }

    /**
     * {@code POST  /aulas} : Create a new aula.
     *
     * @param aula the aula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aula, or with status {@code 400 (Bad Request)} if the aula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Aula> createAula(@RequestBody Aula aula) throws URISyntaxException {
        LOG.debug("REST request to save Aula : {}", aula);
        if (aula.getId() != null) {
            throw new BadRequestAlertException("A new aula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aula = aulaService.save(aula);
        return ResponseEntity.created(new URI("/api/aulas/" + aula.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aula.getId().toString()))
            .body(aula);
    }

    /**
     * {@code PUT  /aulas/:id} : Updates an existing aula.
     *
     * @param id the id of the aula to save.
     * @param aula the aula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aula,
     * or with status {@code 400 (Bad Request)} if the aula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aula> updateAula(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aula aula)
        throws URISyntaxException {
        LOG.debug("REST request to update Aula : {}, {}", id, aula);
        if (aula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aula = aulaService.update(aula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aula.getId().toString()))
            .body(aula);
    }

    /**
     * {@code PATCH  /aulas/:id} : Partial updates given fields of an existing aula, field will ignore if it is null
     *
     * @param id the id of the aula to save.
     * @param aula the aula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aula,
     * or with status {@code 400 (Bad Request)} if the aula is not valid,
     * or with status {@code 404 (Not Found)} if the aula is not found,
     * or with status {@code 500 (Internal Server Error)} if the aula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aula> partialUpdateAula(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aula aula)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Aula partially : {}, {}", id, aula);
        if (aula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aula> result = aulaService.partialUpdate(aula);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aula.getId().toString())
        );
    }

    /**
     * {@code GET  /aulas} : get all the aulas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aulas in body.
     */
    @GetMapping("")
    public List<Aula> getAllAulas() {
        LOG.debug("REST request to get all Aulas");
        return aulaService.findAll();
    }

    /**
     * {@code GET  /aulas/:id} : get the "id" aula.
     *
     * @param id the id of the aula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aula> getAula(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Aula : {}", id);
        Optional<Aula> aula = aulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aula);
    }

    /**
     * {@code DELETE  /aulas/:id} : delete the "id" aula.
     *
     * @param id the id of the aula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAula(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Aula : {}", id);
        aulaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
