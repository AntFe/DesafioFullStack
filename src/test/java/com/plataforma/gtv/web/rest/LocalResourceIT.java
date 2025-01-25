package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.LocalAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Local;
import com.plataforma.gtv.repository.LocalRepository;
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
 * Integration tests for the {@link LocalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalResourceIT {

    private static final String DEFAULT_NOME_DO_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_NOME_DO_LOCAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalMockMvc;

    private Local local;

    private Local insertedLocal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createEntity() {
        return new Local().nomeDoLocal(DEFAULT_NOME_DO_LOCAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createUpdatedEntity() {
        return new Local().nomeDoLocal(UPDATED_NOME_DO_LOCAL);
    }

    @BeforeEach
    public void initTest() {
        local = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocal != null) {
            localRepository.delete(insertedLocal);
            insertedLocal = null;
        }
    }

    @Test
    @Transactional
    void createLocal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Local
        var returnedLocal = om.readValue(
            restLocalMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Local.class
        );

        // Validate the Local in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocalUpdatableFieldsEquals(returnedLocal, getPersistedLocal(returnedLocal));

        insertedLocal = returnedLocal;
    }

    @Test
    @Transactional
    void createLocalWithExistingId() throws Exception {
        // Create the Local with an existing ID
        local.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocals() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        // Get all the localList
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeDoLocal").value(hasItem(DEFAULT_NOME_DO_LOCAL)));
    }

    @Test
    @Transactional
    void getLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc
            .perform(get(ENTITY_API_URL_ID, local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.nomeDoLocal").value(DEFAULT_NOME_DO_LOCAL));
    }

    @Test
    @Transactional
    void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local
        Local updatedLocal = localRepository.findById(local.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocal are not directly saved in db
        em.detach(updatedLocal);
        updatedLocal.nomeDoLocal(UPDATED_NOME_DO_LOCAL);

        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocal.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocalToMatchAllProperties(updatedLocal);
    }

    @Test
    @Transactional
    void putNonExistingLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, local.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLocal, local), getPersistedLocal(local));
    }

    @Test
    @Transactional
    void fullUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal.nomeDoLocal(UPDATED_NOME_DO_LOCAL);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalUpdatableFieldsEquals(partialUpdatedLocal, getPersistedLocal(partialUpdatedLocal));
    }

    @Test
    @Transactional
    void patchNonExistingLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, local.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.saveAndFlush(local);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the local
        restLocalMockMvc
            .perform(delete(ENTITY_API_URL_ID, local.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return localRepository.count();
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

    protected Local getPersistedLocal(Local local) {
        return localRepository.findById(local.getId()).orElseThrow();
    }

    protected void assertPersistedLocalToMatchAllProperties(Local expectedLocal) {
        assertLocalAllPropertiesEquals(expectedLocal, getPersistedLocal(expectedLocal));
    }

    protected void assertPersistedLocalToMatchUpdatableProperties(Local expectedLocal) {
        assertLocalAllUpdatablePropertiesEquals(expectedLocal, getPersistedLocal(expectedLocal));
    }
}
