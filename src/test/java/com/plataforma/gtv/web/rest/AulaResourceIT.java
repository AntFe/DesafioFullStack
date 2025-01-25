package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.AulaAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Aula;
import com.plataforma.gtv.repository.AulaRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AulaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AulaResourceIT {

    private static final String DEFAULT_TITULO_AULA = "AAAAAAAAAA";
    private static final String UPDATED_TITULO_AULA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_VIDEO = "AAAAAAAAAA";
    private static final String UPDATED_LINK_VIDEO = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_ARQUIVOS = "AAAAAAAAAA";
    private static final String UPDATED_LINK_ARQUIVOS = "BBBBBBBBBB";

    private static final String DEFAULT_RESUMO = "AAAAAAAAAA";
    private static final String UPDATED_RESUMO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAulaMockMvc;

    private Aula aula;

    private Aula insertedAula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aula createEntity() {
        return new Aula()
            .tituloAula(DEFAULT_TITULO_AULA)
            .descricao(DEFAULT_DESCRICAO)
            .linkVideo(DEFAULT_LINK_VIDEO)
            .linkArquivos(DEFAULT_LINK_ARQUIVOS)
            .resumo(DEFAULT_RESUMO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aula createUpdatedEntity() {
        return new Aula()
            .tituloAula(UPDATED_TITULO_AULA)
            .descricao(UPDATED_DESCRICAO)
            .linkVideo(UPDATED_LINK_VIDEO)
            .linkArquivos(UPDATED_LINK_ARQUIVOS)
            .resumo(UPDATED_RESUMO);
    }

    @BeforeEach
    public void initTest() {
        aula = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAula != null) {
            aulaRepository.delete(insertedAula);
            insertedAula = null;
        }
    }

    @Test
    @Transactional
    void createAula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Aula
        var returnedAula = om.readValue(
            restAulaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aula)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Aula.class
        );

        // Validate the Aula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAulaUpdatableFieldsEquals(returnedAula, getPersistedAula(returnedAula));

        insertedAula = returnedAula;
    }

    @Test
    @Transactional
    void createAulaWithExistingId() throws Exception {
        // Create the Aula with an existing ID
        aula.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAulaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aula)))
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAulas() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        // Get all the aulaList
        restAulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aula.getId().intValue())))
            .andExpect(jsonPath("$.[*].tituloAula").value(hasItem(DEFAULT_TITULO_AULA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].linkVideo").value(hasItem(DEFAULT_LINK_VIDEO)))
            .andExpect(jsonPath("$.[*].linkArquivos").value(hasItem(DEFAULT_LINK_ARQUIVOS)))
            .andExpect(jsonPath("$.[*].resumo").value(hasItem(DEFAULT_RESUMO)));
    }

    @Test
    @Transactional
    void getAula() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        // Get the aula
        restAulaMockMvc
            .perform(get(ENTITY_API_URL_ID, aula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aula.getId().intValue()))
            .andExpect(jsonPath("$.tituloAula").value(DEFAULT_TITULO_AULA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.linkVideo").value(DEFAULT_LINK_VIDEO))
            .andExpect(jsonPath("$.linkArquivos").value(DEFAULT_LINK_ARQUIVOS))
            .andExpect(jsonPath("$.resumo").value(DEFAULT_RESUMO));
    }

    @Test
    @Transactional
    void getNonExistingAula() throws Exception {
        // Get the aula
        restAulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAula() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aula
        Aula updatedAula = aulaRepository.findById(aula.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAula are not directly saved in db
        em.detach(updatedAula);
        updatedAula
            .tituloAula(UPDATED_TITULO_AULA)
            .descricao(UPDATED_DESCRICAO)
            .linkVideo(UPDATED_LINK_VIDEO)
            .linkArquivos(UPDATED_LINK_ARQUIVOS)
            .resumo(UPDATED_RESUMO);

        restAulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAula.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAula))
            )
            .andExpect(status().isOk());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAulaToMatchAllProperties(updatedAula);
    }

    @Test
    @Transactional
    void putNonExistingAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aula.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAulaWithPatch() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aula using partial update
        Aula partialUpdatedAula = new Aula();
        partialUpdatedAula.setId(aula.getId());

        partialUpdatedAula.linkVideo(UPDATED_LINK_VIDEO);

        restAulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAula.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAula))
            )
            .andExpect(status().isOk());

        // Validate the Aula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAulaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAula, aula), getPersistedAula(aula));
    }

    @Test
    @Transactional
    void fullUpdateAulaWithPatch() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aula using partial update
        Aula partialUpdatedAula = new Aula();
        partialUpdatedAula.setId(aula.getId());

        partialUpdatedAula
            .tituloAula(UPDATED_TITULO_AULA)
            .descricao(UPDATED_DESCRICAO)
            .linkVideo(UPDATED_LINK_VIDEO)
            .linkArquivos(UPDATED_LINK_ARQUIVOS)
            .resumo(UPDATED_RESUMO);

        restAulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAula.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAula))
            )
            .andExpect(status().isOk());

        // Validate the Aula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAulaUpdatableFieldsEquals(partialUpdatedAula, getPersistedAula(partialUpdatedAula));
    }

    @Test
    @Transactional
    void patchNonExistingAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aula.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAulaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAula() throws Exception {
        // Initialize the database
        insertedAula = aulaRepository.saveAndFlush(aula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aula
        restAulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, aula.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aulaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Aula getPersistedAula(Aula aula) {
        return aulaRepository.findById(aula.getId()).orElseThrow();
    }

    protected void assertPersistedAulaToMatchAllProperties(Aula expectedAula) {
        assertAulaAllPropertiesEquals(expectedAula, getPersistedAula(expectedAula));
    }

    protected void assertPersistedAulaToMatchUpdatableProperties(Aula expectedAula) {
        assertAulaAllUpdatablePropertiesEquals(expectedAula, getPersistedAula(expectedAula));
    }
}
