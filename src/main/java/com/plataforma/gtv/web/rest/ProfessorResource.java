package com.plataforma.gtv.web.rest;

import com.plataforma.gtv.domain.Professor;
import com.plataforma.gtv.repository.ProfessorRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.plataforma.gtv.domain.Professor}.
 */
@RestController
@RequestMapping("/api/professors")
@Transactional
public class ProfessorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessorResource.class);

    private static final String ENTITY_NAME = "professor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessorRepository professorRepository;

    public ProfessorResource(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /**
     * {@code POST  /professors} : Create a new professor.
     *
     * @param professor the professor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professor, or with status {@code 400 (Bad Request)} if the professor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) throws URISyntaxException {
        LOG.debug("REST request to save Professor : {}", professor);
        if (professor.getId() != null) {
            throw new BadRequestAlertException("A new professor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professor = professorRepository.save(professor);
        return ResponseEntity.created(new URI("/api/professors/" + professor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, professor.getId().toString()))
            .body(professor);
    }

    /**
     * {@code PUT  /professors/:id} : Updates an existing professor.
     *
     * @param id the id of the professor to save.
     * @param professor the professor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professor,
     * or with status {@code 400 (Bad Request)} if the professor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Professor professor
    ) throws URISyntaxException {
        LOG.debug("REST request to update Professor : {}, {}", id, professor);
        if (professor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professor = professorRepository.save(professor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professor.getId().toString()))
            .body(professor);
    }

    /**
     * {@code PATCH  /professors/:id} : Partial updates given fields of an existing professor, field will ignore if it is null
     *
     * @param id the id of the professor to save.
     * @param professor the professor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professor,
     * or with status {@code 400 (Bad Request)} if the professor is not valid,
     * or with status {@code 404 (Not Found)} if the professor is not found,
     * or with status {@code 500 (Internal Server Error)} if the professor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Professor> partialUpdateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Professor professor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Professor partially : {}, {}", id, professor);
        if (professor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Professor> result = professorRepository
            .findById(professor.getId())
            .map(existingProfessor -> {
                if (professor.getNome() != null) {
                    existingProfessor.setNome(professor.getNome());
                }
                if (professor.getSobrenome() != null) {
                    existingProfessor.setSobrenome(professor.getSobrenome());
                }
                if (professor.getEmail() != null) {
                    existingProfessor.setEmail(professor.getEmail());
                }
                if (professor.getNumeroTelefone() != null) {
                    existingProfessor.setNumeroTelefone(professor.getNumeroTelefone());
                }
                if (professor.getIngresso() != null) {
                    existingProfessor.setIngresso(professor.getIngresso());
                }
                if (professor.getMateriaLecionada() != null) {
                    existingProfessor.setMateriaLecionada(professor.getMateriaLecionada());
                }
                if (professor.getRegistroProfissional() != null) {
                    existingProfessor.setRegistroProfissional(professor.getRegistroProfissional());
                }

                return existingProfessor;
            })
            .map(professorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professor.getId().toString())
        );
    }

    /**
     * {@code GET  /professors} : get all the professors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Professor>> getAllProfessors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Professors");
        Page<Professor> page = professorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professors/:id} : get the "id" professor.
     *
     * @param id the id of the professor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Professor : {}", id);
        Optional<Professor> professor = professorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(professor);
    }

    /**
     * {@code DELETE  /professors/:id} : delete the "id" professor.
     *
     * @param id the id of the professor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Professor : {}", id);
        professorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
