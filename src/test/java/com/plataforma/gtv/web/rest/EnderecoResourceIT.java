package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.EnderecoAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Endereco;
import com.plataforma.gtv.repository.EnderecoRepository;
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
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnderecoResourceIT {

    private static final String DEFAULT_RUA = "AAAAAAAAAA";
    private static final String UPDATED_RUA = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enderecos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    private Endereco insertedEndereco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity() {
        return new Endereco().rua(DEFAULT_RUA).cep(DEFAULT_CEP).cidade(DEFAULT_CIDADE).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity() {
        return new Endereco().rua(UPDATED_RUA).cep(UPDATED_CEP).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    public void initTest() {
        endereco = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEndereco != null) {
            enderecoRepository.delete(insertedEndereco);
            insertedEndereco = null;
        }
    }

    @Test
    @Transactional
    void createEndereco() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Endereco
        var returnedEndereco = om.readValue(
            restEnderecoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(endereco)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Endereco.class
        );

        // Validate the Endereco in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEnderecoUpdatableFieldsEquals(returnedEndereco, getPersistedEndereco(returnedEndereco));

        insertedEndereco = returnedEndereco;
    }

    @Test
    @Transactional
    void createEnderecoWithExistingId() throws Exception {
        // Create the Endereco with an existing ID
        endereco.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(endereco)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnderecos() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL_ID, endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.rua").value(DEFAULT_RUA))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEndereco are not directly saved in db
        em.detach(updatedEndereco);
        updatedEndereco.rua(UPDATED_RUA).cep(UPDATED_CEP).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO);

        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEndereco.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnderecoToMatchAllProperties(updatedEndereco);
    }

    @Test
    @Transactional
    void putNonExistingEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, endereco.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(endereco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(endereco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(endereco)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco.rua(UPDATED_RUA).cep(UPDATED_CEP).cidade(UPDATED_CIDADE);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnderecoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEndereco, endereco), getPersistedEndereco(endereco));
    }

    @Test
    @Transactional
    void fullUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco.rua(UPDATED_RUA).cep(UPDATED_CEP).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnderecoUpdatableFieldsEquals(partialUpdatedEndereco, getPersistedEndereco(partialUpdatedEndereco));
    }

    @Test
    @Transactional
    void patchNonExistingEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, endereco.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(endereco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(endereco))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(endereco)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the endereco
        restEnderecoMockMvc
            .perform(delete(ENTITY_API_URL_ID, endereco.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enderecoRepository.count();
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

    protected Endereco getPersistedEndereco(Endereco endereco) {
        return enderecoRepository.findById(endereco.getId()).orElseThrow();
    }

    protected void assertPersistedEnderecoToMatchAllProperties(Endereco expectedEndereco) {
        assertEnderecoAllPropertiesEquals(expectedEndereco, getPersistedEndereco(expectedEndereco));
    }

    protected void assertPersistedEnderecoToMatchUpdatableProperties(Endereco expectedEndereco) {
        assertEnderecoAllUpdatablePropertiesEquals(expectedEndereco, getPersistedEndereco(expectedEndereco));
    }
}
