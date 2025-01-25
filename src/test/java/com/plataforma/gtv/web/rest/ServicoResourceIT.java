package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.ServicoAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Servico;
import com.plataforma.gtv.repository.ServicoRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ServicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServicoResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOME_DO_SERVICO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_DO_SERVICO = "BBBBBBBBBB";

    private static final String DEFAULT_RESUMO = "AAAAAAAAAA";
    private static final String UPDATED_RESUMO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/servicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicoMockMvc;

    private Servico servico;

    private Servico insertedServico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servico createEntity() {
        return new Servico()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .nomeDoServico(DEFAULT_NOME_DO_SERVICO)
            .resumo(DEFAULT_RESUMO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servico createUpdatedEntity() {
        return new Servico()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nomeDoServico(UPDATED_NOME_DO_SERVICO)
            .resumo(UPDATED_RESUMO);
    }

    @BeforeEach
    public void initTest() {
        servico = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedServico != null) {
            servicoRepository.delete(insertedServico);
            insertedServico = null;
        }
    }

    @Test
    @Transactional
    void createServico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Servico
        var returnedServico = om.readValue(
            restServicoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servico)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Servico.class
        );

        // Validate the Servico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertServicoUpdatableFieldsEquals(returnedServico, getPersistedServico(returnedServico));

        insertedServico = returnedServico;
    }

    @Test
    @Transactional
    void createServicoWithExistingId() throws Exception {
        // Create the Servico with an existing ID
        servico.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servico)))
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServicos() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        // Get all the servicoList
        restServicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servico.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].nomeDoServico").value(hasItem(DEFAULT_NOME_DO_SERVICO)))
            .andExpect(jsonPath("$.[*].resumo").value(hasItem(DEFAULT_RESUMO)));
    }

    @Test
    @Transactional
    void getServico() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        // Get the servico
        restServicoMockMvc
            .perform(get(ENTITY_API_URL_ID, servico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servico.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.nomeDoServico").value(DEFAULT_NOME_DO_SERVICO))
            .andExpect(jsonPath("$.resumo").value(DEFAULT_RESUMO));
    }

    @Test
    @Transactional
    void getNonExistingServico() throws Exception {
        // Get the servico
        restServicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServico() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servico
        Servico updatedServico = servicoRepository.findById(servico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServico are not directly saved in db
        em.detach(updatedServico);
        updatedServico
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nomeDoServico(UPDATED_NOME_DO_SERVICO)
            .resumo(UPDATED_RESUMO);

        restServicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedServico))
            )
            .andExpect(status().isOk());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicoToMatchAllProperties(updatedServico);
    }

    @Test
    @Transactional
    void putNonExistingServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servico)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicoWithPatch() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servico using partial update
        Servico partialUpdatedServico = new Servico();
        partialUpdatedServico.setId(servico.getId());

        partialUpdatedServico.startDate(UPDATED_START_DATE).nomeDoServico(UPDATED_NOME_DO_SERVICO).resumo(UPDATED_RESUMO);

        restServicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServico))
            )
            .andExpect(status().isOk());

        // Validate the Servico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedServico, servico), getPersistedServico(servico));
    }

    @Test
    @Transactional
    void fullUpdateServicoWithPatch() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servico using partial update
        Servico partialUpdatedServico = new Servico();
        partialUpdatedServico.setId(servico.getId());

        partialUpdatedServico
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nomeDoServico(UPDATED_NOME_DO_SERVICO)
            .resumo(UPDATED_RESUMO);

        restServicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServico))
            )
            .andExpect(status().isOk());

        // Validate the Servico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicoUpdatableFieldsEquals(partialUpdatedServico, getPersistedServico(partialUpdatedServico));
    }

    @Test
    @Transactional
    void patchNonExistingServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servico.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servico)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServico() throws Exception {
        // Initialize the database
        insertedServico = servicoRepository.saveAndFlush(servico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servico
        restServicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, servico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicoRepository.count();
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

    protected Servico getPersistedServico(Servico servico) {
        return servicoRepository.findById(servico.getId()).orElseThrow();
    }

    protected void assertPersistedServicoToMatchAllProperties(Servico expectedServico) {
        assertServicoAllPropertiesEquals(expectedServico, getPersistedServico(expectedServico));
    }

    protected void assertPersistedServicoToMatchUpdatableProperties(Servico expectedServico) {
        assertServicoAllUpdatablePropertiesEquals(expectedServico, getPersistedServico(expectedServico));
    }
}
