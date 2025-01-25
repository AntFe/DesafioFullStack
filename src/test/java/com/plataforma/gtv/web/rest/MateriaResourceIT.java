package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.MateriaAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Materia;
import com.plataforma.gtv.repository.MateriaRepository;
import com.plataforma.gtv.service.MateriaService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MateriaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MateriaResourceIT {

    private static final String DEFAULT_NOME_DA_MATERIA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_DA_MATERIA = "BBBBBBBBBB";

    private static final String DEFAULT_EMENTA = "AAAAAAAAAA";
    private static final String UPDATED_EMENTA = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIAS_BIBLIOGRAFICAS = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIAS_BIBLIOGRAFICAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/materias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MateriaRepository materiaRepository;

    @Mock
    private MateriaRepository materiaRepositoryMock;

    @Mock
    private MateriaService materiaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMateriaMockMvc;

    private Materia materia;

    private Materia insertedMateria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materia createEntity() {
        return new Materia()
            .nomeDaMateria(DEFAULT_NOME_DA_MATERIA)
            .ementa(DEFAULT_EMENTA)
            .referenciasBibliograficas(DEFAULT_REFERENCIAS_BIBLIOGRAFICAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materia createUpdatedEntity() {
        return new Materia()
            .nomeDaMateria(UPDATED_NOME_DA_MATERIA)
            .ementa(UPDATED_EMENTA)
            .referenciasBibliograficas(UPDATED_REFERENCIAS_BIBLIOGRAFICAS);
    }

    @BeforeEach
    public void initTest() {
        materia = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMateria != null) {
            materiaRepository.delete(insertedMateria);
            insertedMateria = null;
        }
    }

    @Test
    @Transactional
    void createMateria() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Materia
        var returnedMateria = om.readValue(
            restMateriaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materia)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Materia.class
        );

        // Validate the Materia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMateriaUpdatableFieldsEquals(returnedMateria, getPersistedMateria(returnedMateria));

        insertedMateria = returnedMateria;
    }

    @Test
    @Transactional
    void createMateriaWithExistingId() throws Exception {
        // Create the Materia with an existing ID
        materia.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMateriaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materia)))
            .andExpect(status().isBadRequest());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterias() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        // Get all the materiaList
        restMateriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeDaMateria").value(hasItem(DEFAULT_NOME_DA_MATERIA)))
            .andExpect(jsonPath("$.[*].ementa").value(hasItem(DEFAULT_EMENTA)))
            .andExpect(jsonPath("$.[*].referenciasBibliograficas").value(hasItem(DEFAULT_REFERENCIAS_BIBLIOGRAFICAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMateriasWithEagerRelationshipsIsEnabled() throws Exception {
        when(materiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMateriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(materiaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMateriasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(materiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMateriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(materiaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMateria() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        // Get the materia
        restMateriaMockMvc
            .perform(get(ENTITY_API_URL_ID, materia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materia.getId().intValue()))
            .andExpect(jsonPath("$.nomeDaMateria").value(DEFAULT_NOME_DA_MATERIA))
            .andExpect(jsonPath("$.ementa").value(DEFAULT_EMENTA))
            .andExpect(jsonPath("$.referenciasBibliograficas").value(DEFAULT_REFERENCIAS_BIBLIOGRAFICAS));
    }

    @Test
    @Transactional
    void getNonExistingMateria() throws Exception {
        // Get the materia
        restMateriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMateria() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materia
        Materia updatedMateria = materiaRepository.findById(materia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMateria are not directly saved in db
        em.detach(updatedMateria);
        updatedMateria
            .nomeDaMateria(UPDATED_NOME_DA_MATERIA)
            .ementa(UPDATED_EMENTA)
            .referenciasBibliograficas(UPDATED_REFERENCIAS_BIBLIOGRAFICAS);

        restMateriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMateria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMateria))
            )
            .andExpect(status().isOk());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMateriaToMatchAllProperties(updatedMateria);
    }

    @Test
    @Transactional
    void putNonExistingMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materia.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMateriaWithPatch() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materia using partial update
        Materia partialUpdatedMateria = new Materia();
        partialUpdatedMateria.setId(materia.getId());

        partialUpdatedMateria
            .nomeDaMateria(UPDATED_NOME_DA_MATERIA)
            .ementa(UPDATED_EMENTA)
            .referenciasBibliograficas(UPDATED_REFERENCIAS_BIBLIOGRAFICAS);

        restMateriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateria))
            )
            .andExpect(status().isOk());

        // Validate the Materia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMateriaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMateria, materia), getPersistedMateria(materia));
    }

    @Test
    @Transactional
    void fullUpdateMateriaWithPatch() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materia using partial update
        Materia partialUpdatedMateria = new Materia();
        partialUpdatedMateria.setId(materia.getId());

        partialUpdatedMateria
            .nomeDaMateria(UPDATED_NOME_DA_MATERIA)
            .ementa(UPDATED_EMENTA)
            .referenciasBibliograficas(UPDATED_REFERENCIAS_BIBLIOGRAFICAS);

        restMateriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateria))
            )
            .andExpect(status().isOk());

        // Validate the Materia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMateriaUpdatableFieldsEquals(partialUpdatedMateria, getPersistedMateria(partialUpdatedMateria));
    }

    @Test
    @Transactional
    void patchNonExistingMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMateria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMateriaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMateria() throws Exception {
        // Initialize the database
        insertedMateria = materiaRepository.saveAndFlush(materia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materia
        restMateriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, materia.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materiaRepository.count();
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

    protected Materia getPersistedMateria(Materia materia) {
        return materiaRepository.findById(materia.getId()).orElseThrow();
    }

    protected void assertPersistedMateriaToMatchAllProperties(Materia expectedMateria) {
        assertMateriaAllPropertiesEquals(expectedMateria, getPersistedMateria(expectedMateria));
    }

    protected void assertPersistedMateriaToMatchUpdatableProperties(Materia expectedMateria) {
        assertMateriaAllUpdatablePropertiesEquals(expectedMateria, getPersistedMateria(expectedMateria));
    }
}
