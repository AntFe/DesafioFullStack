package com.plataforma.gtv.web.rest;

import com.plataforma.gtv.domain.Servico;
import com.plataforma.gtv.repository.ServicoRepository;
import com.plataforma.gtv.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.plataforma.gtv.domain.Servico}.
 */
@RestController
@RequestMapping("/api/servicos")
@Transactional
public class ServicoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServicoResource.class);

    private static final String ENTITY_NAME = "servico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicoRepository servicoRepository;

    public ServicoResource(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    /**
     * {@code POST  /servicos} : Create a new servico.
     *
     * @param servico the servico to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servico, or with status {@code 400 (Bad Request)} if the servico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Servico> createServico(@RequestBody Servico servico) throws URISyntaxException {
        LOG.debug("REST request to save Servico : {}", servico);
        if (servico.getId() != null) {
            throw new BadRequestAlertException("A new servico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        servico = servicoRepository.save(servico);
        return ResponseEntity.created(new URI("/api/servicos/" + servico.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, servico.getId().toString()))
            .body(servico);
    }

    /**
     * {@code PUT  /servicos/:id} : Updates an existing servico.
     *
     * @param id the id of the servico to save.
     * @param servico the servico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servico,
     * or with status {@code 400 (Bad Request)} if the servico is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Servico> updateServico(@PathVariable(value = "id", required = false) final Long id, @RequestBody Servico servico)
        throws URISyntaxException {
        LOG.debug("REST request to update Servico : {}, {}", id, servico);
        if (servico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        servico = servicoRepository.save(servico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servico.getId().toString()))
            .body(servico);
    }

    /**
     * {@code PATCH  /servicos/:id} : Partial updates given fields of an existing servico, field will ignore if it is null
     *
     * @param id the id of the servico to save.
     * @param servico the servico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servico,
     * or with status {@code 400 (Bad Request)} if the servico is not valid,
     * or with status {@code 404 (Not Found)} if the servico is not found,
     * or with status {@code 500 (Internal Server Error)} if the servico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Servico> partialUpdateServico(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Servico servico
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Servico partially : {}, {}", id, servico);
        if (servico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Servico> result = servicoRepository
            .findById(servico.getId())
            .map(existingServico -> {
                if (servico.getStartDate() != null) {
                    existingServico.setStartDate(servico.getStartDate());
                }
                if (servico.getEndDate() != null) {
                    existingServico.setEndDate(servico.getEndDate());
                }
                if (servico.getNomeDoServico() != null) {
                    existingServico.setNomeDoServico(servico.getNomeDoServico());
                }
                if (servico.getResumo() != null) {
                    existingServico.setResumo(servico.getResumo());
                }

                return existingServico;
            })
            .map(servicoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servico.getId().toString())
        );
    }

    /**
     * {@code GET  /servicos} : get all the servicos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Servico>> getAllServicos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("professor-is-null".equals(filter)) {
            LOG.debug("REST request to get all Servicos where professor is null");
            return new ResponseEntity<>(
                StreamSupport.stream(servicoRepository.findAll().spliterator(), false)
                    .filter(servico -> servico.getProfessor() == null)
                    .toList(),
                HttpStatus.OK
            );
        }
        LOG.debug("REST request to get a page of Servicos");
        Page<Servico> page = servicoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /servicos/:id} : get the "id" servico.
     *
     * @param id the id of the servico to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servico, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Servico> getServico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Servico : {}", id);
        Optional<Servico> servico = servicoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(servico);
    }

    /**
     * {@code DELETE  /servicos/:id} : delete the "id" servico.
     *
     * @param id the id of the servico to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Servico : {}", id);
        servicoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
