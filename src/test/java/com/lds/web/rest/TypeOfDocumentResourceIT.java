package com.lds.web.rest;

import static com.lds.domain.TypeOfDocumentAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.TypeOfDocument;
import com.lds.repository.TypeOfDocumentRepository;
import com.lds.service.dto.TypeOfDocumentDTO;
import com.lds.service.mapper.TypeOfDocumentMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeOfDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeOfDocumentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-of-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeOfDocumentRepository typeOfDocumentRepository;

    @Autowired
    private TypeOfDocumentMapper typeOfDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOfDocumentMockMvc;

    private TypeOfDocument typeOfDocument;

    private TypeOfDocument insertedTypeOfDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfDocument createEntity() {
        return new TypeOfDocument().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfDocument createUpdatedEntity() {
        return new TypeOfDocument().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        typeOfDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeOfDocument != null) {
            typeOfDocumentRepository.delete(insertedTypeOfDocument);
            insertedTypeOfDocument = null;
        }
    }

    @Test
    @Transactional
    void createTypeOfDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);
        var returnedTypeOfDocumentDTO = om.readValue(
            restTypeOfDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeOfDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeOfDocumentDTO.class
        );

        // Validate the TypeOfDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeOfDocument = typeOfDocumentMapper.toEntity(returnedTypeOfDocumentDTO);
        assertTypeOfDocumentUpdatableFieldsEquals(returnedTypeOfDocument, getPersistedTypeOfDocument(returnedTypeOfDocument));

        insertedTypeOfDocument = returnedTypeOfDocument;
    }

    @Test
    @Transactional
    void createTypeOfDocumentWithExistingId() throws Exception {
        // Create the TypeOfDocument with an existing ID
        typeOfDocument.setId(1L);
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOfDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeOfDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeOfDocument.setName(null);

        // Create the TypeOfDocument, which fails.
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        restTypeOfDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeOfDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeOfDocuments() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        // Get all the typeOfDocumentList
        restTypeOfDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTypeOfDocument() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        // Get the typeOfDocument
        restTypeOfDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, typeOfDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOfDocument.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTypeOfDocument() throws Exception {
        // Get the typeOfDocument
        restTypeOfDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeOfDocument() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeOfDocument
        TypeOfDocument updatedTypeOfDocument = typeOfDocumentRepository.findById(typeOfDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeOfDocument are not directly saved in db
        em.detach(updatedTypeOfDocument);
        updatedTypeOfDocument.name(UPDATED_NAME);
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(updatedTypeOfDocument);

        restTypeOfDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeOfDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeOfDocumentToMatchAllProperties(updatedTypeOfDocument);
    }

    @Test
    @Transactional
    void putNonExistingTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeOfDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeOfDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeOfDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeOfDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeOfDocument using partial update
        TypeOfDocument partialUpdatedTypeOfDocument = new TypeOfDocument();
        partialUpdatedTypeOfDocument.setId(typeOfDocument.getId());

        restTypeOfDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeOfDocument))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeOfDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeOfDocument, typeOfDocument),
            getPersistedTypeOfDocument(typeOfDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeOfDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeOfDocument using partial update
        TypeOfDocument partialUpdatedTypeOfDocument = new TypeOfDocument();
        partialUpdatedTypeOfDocument.setId(typeOfDocument.getId());

        partialUpdatedTypeOfDocument.name(UPDATED_NAME);

        restTypeOfDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeOfDocument))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeOfDocumentUpdatableFieldsEquals(partialUpdatedTypeOfDocument, getPersistedTypeOfDocument(partialUpdatedTypeOfDocument));
    }

    @Test
    @Transactional
    void patchNonExistingTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeOfDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeOfDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeOfDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeOfDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeOfDocument.setId(longCount.incrementAndGet());

        // Create the TypeOfDocument
        TypeOfDocumentDTO typeOfDocumentDTO = typeOfDocumentMapper.toDto(typeOfDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeOfDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeOfDocument() throws Exception {
        // Initialize the database
        insertedTypeOfDocument = typeOfDocumentRepository.saveAndFlush(typeOfDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeOfDocument
        restTypeOfDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeOfDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeOfDocumentRepository.count();
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

    protected TypeOfDocument getPersistedTypeOfDocument(TypeOfDocument typeOfDocument) {
        return typeOfDocumentRepository.findById(typeOfDocument.getId()).orElseThrow();
    }

    protected void assertPersistedTypeOfDocumentToMatchAllProperties(TypeOfDocument expectedTypeOfDocument) {
        assertTypeOfDocumentAllPropertiesEquals(expectedTypeOfDocument, getPersistedTypeOfDocument(expectedTypeOfDocument));
    }

    protected void assertPersistedTypeOfDocumentToMatchUpdatableProperties(TypeOfDocument expectedTypeOfDocument) {
        assertTypeOfDocumentAllUpdatablePropertiesEquals(expectedTypeOfDocument, getPersistedTypeOfDocument(expectedTypeOfDocument));
    }
}
