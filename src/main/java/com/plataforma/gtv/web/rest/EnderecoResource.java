package com.plataforma.gtv.web.rest;

import com.plataforma.gtv.domain.Endereco;
import com.plataforma.gtv.repository.EnderecoRepository;
import com.plataforma.gtv.service.EnderecoService;
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
 * REST controller for managing {@link com.plataforma.gtv.domain.Endereco}.
 */
@RestController
@RequestMapping("/api/enderecos")
public class EnderecoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnderecoResource.class);

    private static final String ENTITY_NAME = "endereco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnderecoService enderecoService;

    private final EnderecoRepository enderecoRepository;

    public EnderecoResource(EnderecoService enderecoService, EnderecoRepository enderecoRepository) {
        this.enderecoService = enderecoService;
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * {@code POST  /enderecos} : Create a new endereco.
     *
     * @param endereco the endereco to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new endereco, or with status {@code 400 (Bad Request)} if the endereco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Endereco> createEndereco(@RequestBody Endereco endereco) throws URISyntaxException {
        LOG.debug("REST request to save Endereco : {}", endereco);
        if (endereco.getId() != null) {
            throw new BadRequestAlertException("A new endereco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        endereco = enderecoService.save(endereco);
        return ResponseEntity.created(new URI("/api/enderecos/" + endereco.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, endereco.getId().toString()))
            .body(endereco);
    }

    /**
     * {@code PUT  /enderecos/:id} : Updates an existing endereco.
     *
     * @param id the id of the endereco to save.
     * @param endereco the endereco to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endereco,
     * or with status {@code 400 (Bad Request)} if the endereco is not valid,
     * or with status {@code 500 (Internal Server Error)} if the endereco couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> updateEndereco(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Endereco endereco
    ) throws URISyntaxException {
        LOG.debug("REST request to update Endereco : {}, {}", id, endereco);
        if (endereco.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endereco.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        endereco = enderecoService.update(endereco);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endereco.getId().toString()))
            .body(endereco);
    }

    /**
     * {@code PATCH  /enderecos/:id} : Partial updates given fields of an existing endereco, field will ignore if it is null
     *
     * @param id the id of the endereco to save.
     * @param endereco the endereco to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endereco,
     * or with status {@code 400 (Bad Request)} if the endereco is not valid,
     * or with status {@code 404 (Not Found)} if the endereco is not found,
     * or with status {@code 500 (Internal Server Error)} if the endereco couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Endereco> partialUpdateEndereco(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Endereco endereco
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Endereco partially : {}, {}", id, endereco);
        if (endereco.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endereco.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Endereco> result = enderecoService.partialUpdate(endereco);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endereco.getId().toString())
        );
    }

    /**
     * {@code GET  /enderecos} : get all the enderecos.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enderecos in body.
     */
    @GetMapping("")
    public List<Endereco> getAllEnderecos(@RequestParam(name = "filter", required = false) String filter) {
        if ("aluno-is-null".equals(filter)) {
            LOG.debug("REST request to get all Enderecos where aluno is null");
            return enderecoService.findAllWhereAlunoIsNull();
        }
        LOG.debug("REST request to get all Enderecos");
        return enderecoService.findAll();
    }

    /**
     * {@code GET  /enderecos/:id} : get the "id" endereco.
     *
     * @param id the id of the endereco to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the endereco, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> getEndereco(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Endereco : {}", id);
        Optional<Endereco> endereco = enderecoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(endereco);
    }

    /**
     * {@code DELETE  /enderecos/:id} : delete the "id" endereco.
     *
     * @param id the id of the endereco to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Endereco : {}", id);
        enderecoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
