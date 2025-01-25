package com.plataforma.gtv.web.rest;

import static com.plataforma.gtv.domain.CursoAsserts.*;
import static com.plataforma.gtv.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma.gtv.IntegrationTest;
import com.plataforma.gtv.domain.Curso;
import com.plataforma.gtv.repository.CursoRepository;
import com.plataforma.gtv.service.CursoService;
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
 * Integration tests for the {@link CursoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CursoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CursoRepository cursoRepository;

    @Mock
    private CursoRepository cursoRepositoryMock;

    @Mock
    private CursoService cursoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCursoMockMvc;

    private Curso curso;

    private Curso insertedCurso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createEntity() {
        return new Curso().nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createUpdatedEntity() {
        return new Curso().nome(UPDATED_NOME);
    }

    @BeforeEach
    public void initTest() {
        curso = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCurso != null) {
            cursoRepository.delete(insertedCurso);
            insertedCurso = null;
        }
    }

    @Test
    @Transactional
    void createCurso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Curso
        var returnedCurso = om.readValue(
            restCursoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curso)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Curso.class
        );

        // Validate the Curso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCursoUpdatableFieldsEquals(returnedCurso, getPersistedCurso(returnedCurso));

        insertedCurso = returnedCurso;
    }

    @Test
    @Transactional
    void createCursoWithExistingId() throws Exception {
        // Create the Curso with an existing ID
        curso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curso)))
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curso.setNome(null);

        // Create the Curso, which fails.

        restCursoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curso)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCursos() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        // Get all the cursoList
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursosWithEagerRelationshipsIsEnabled() throws Exception {
        when(cursoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCursoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cursoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cursoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCursoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cursoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        // Get the curso
        restCursoMockMvc
            .perform(get(ENTITY_API_URL_ID, curso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(curso.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingCurso() throws Exception {
        // Get the curso
        restCursoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso
        Curso updatedCurso = cursoRepository.findById(curso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCurso are not directly saved in db
        em.detach(updatedCurso);
        updatedCurso.nome(UPDATED_NOME);

        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurso.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCursoToMatchAllProperties(updatedCurso);
    }

    @Test
    @Transactional
    void putNonExistingCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, curso.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        partialUpdatedCurso.nome(UPDATED_NOME);

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCurso, curso), getPersistedCurso(curso));
    }

    @Test
    @Transactional
    void fullUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        partialUpdatedCurso.nome(UPDATED_NOME);

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursoUpdatableFieldsEquals(partialUpdatedCurso, getPersistedCurso(partialUpdatedCurso));
    }

    @Test
    @Transactional
    void patchNonExistingCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, curso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(curso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.saveAndFlush(curso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the curso
        restCursoMockMvc
            .perform(delete(ENTITY_API_URL_ID, curso.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cursoRepository.count();
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

    protected Curso getPersistedCurso(Curso curso) {
        return cursoRepository.findById(curso.getId()).orElseThrow();
    }

    protected void assertPersistedCursoToMatchAllProperties(Curso expectedCurso) {
        assertCursoAllPropertiesEquals(expectedCurso, getPersistedCurso(expectedCurso));
    }

    protected void assertPersistedCursoToMatchUpdatableProperties(Curso expectedCurso) {
        assertCursoAllUpdatablePropertiesEquals(expectedCurso, getPersistedCurso(expectedCurso));
    }
}
