package com.lds.web.rest;

import static com.lds.domain.DocumentStatusAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.DocumentStatus;
import com.lds.repository.DocumentStatusRepository;
import com.lds.service.dto.DocumentStatusDTO;
import com.lds.service.mapper.DocumentStatusMapper;
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
 * Integration tests for the {@link DocumentStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WARNING = false;
    private static final Boolean UPDATED_WARNING = true;

    private static final String ENTITY_API_URL = "/api/document-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentStatusRepository documentStatusRepository;

    @Autowired
    private DocumentStatusMapper documentStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentStatusMockMvc;

    private DocumentStatus documentStatus;

    private DocumentStatus insertedDocumentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentStatus createEntity() {
        return new DocumentStatus().name(DEFAULT_NAME).color(DEFAULT_COLOR).warning(DEFAULT_WARNING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentStatus createUpdatedEntity() {
        return new DocumentStatus().name(UPDATED_NAME).color(UPDATED_COLOR).warning(UPDATED_WARNING);
    }

    @BeforeEach
    void initTest() {
        documentStatus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentStatus != null) {
            documentStatusRepository.delete(insertedDocumentStatus);
            insertedDocumentStatus = null;
        }
    }

    @Test
    @Transactional
    void createDocumentStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);
        var returnedDocumentStatusDTO = om.readValue(
            restDocumentStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentStatusDTO.class
        );

        // Validate the DocumentStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentStatus = documentStatusMapper.toEntity(returnedDocumentStatusDTO);
        assertDocumentStatusUpdatableFieldsEquals(returnedDocumentStatus, getPersistedDocumentStatus(returnedDocumentStatus));

        insertedDocumentStatus = returnedDocumentStatus;
    }

    @Test
    @Transactional
    void createDocumentStatusWithExistingId() throws Exception {
        // Create the DocumentStatus with an existing ID
        documentStatus.setId(1L);
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentStatus.setName(null);

        // Create the DocumentStatus, which fails.
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        restDocumentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkColorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentStatus.setColor(null);

        // Create the DocumentStatus, which fails.
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        restDocumentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentStatuses() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        // Get all the documentStatusList
        restDocumentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].warning").value(hasItem(DEFAULT_WARNING)));
    }

    @Test
    @Transactional
    void getDocumentStatus() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        // Get the documentStatus
        restDocumentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, documentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.warning").value(DEFAULT_WARNING));
    }

    @Test
    @Transactional
    void getNonExistingDocumentStatus() throws Exception {
        // Get the documentStatus
        restDocumentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentStatus() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatus
        DocumentStatus updatedDocumentStatus = documentStatusRepository.findById(documentStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentStatus are not directly saved in db
        em.detach(updatedDocumentStatus);
        updatedDocumentStatus.name(UPDATED_NAME).color(UPDATED_COLOR).warning(UPDATED_WARNING);
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(updatedDocumentStatus);

        restDocumentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentStatusToMatchAllProperties(updatedDocumentStatus);
    }

    @Test
    @Transactional
    void putNonExistingDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatus using partial update
        DocumentStatus partialUpdatedDocumentStatus = new DocumentStatus();
        partialUpdatedDocumentStatus.setId(documentStatus.getId());

        partialUpdatedDocumentStatus.color(UPDATED_COLOR);

        restDocumentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentStatus))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentStatus, documentStatus),
            getPersistedDocumentStatus(documentStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatus using partial update
        DocumentStatus partialUpdatedDocumentStatus = new DocumentStatus();
        partialUpdatedDocumentStatus.setId(documentStatus.getId());

        partialUpdatedDocumentStatus.name(UPDATED_NAME).color(UPDATED_COLOR).warning(UPDATED_WARNING);

        restDocumentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentStatus))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentStatusUpdatableFieldsEquals(partialUpdatedDocumentStatus, getPersistedDocumentStatus(partialUpdatedDocumentStatus));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatus.setId(longCount.incrementAndGet());

        // Create the DocumentStatus
        DocumentStatusDTO documentStatusDTO = documentStatusMapper.toDto(documentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentStatus() throws Exception {
        // Initialize the database
        insertedDocumentStatus = documentStatusRepository.saveAndFlush(documentStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentStatus
        restDocumentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentStatusRepository.count();
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

    protected DocumentStatus getPersistedDocumentStatus(DocumentStatus documentStatus) {
        return documentStatusRepository.findById(documentStatus.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentStatusToMatchAllProperties(DocumentStatus expectedDocumentStatus) {
        assertDocumentStatusAllPropertiesEquals(expectedDocumentStatus, getPersistedDocumentStatus(expectedDocumentStatus));
    }

    protected void assertPersistedDocumentStatusToMatchUpdatableProperties(DocumentStatus expectedDocumentStatus) {
        assertDocumentStatusAllUpdatablePropertiesEquals(expectedDocumentStatus, getPersistedDocumentStatus(expectedDocumentStatus));
    }
}
