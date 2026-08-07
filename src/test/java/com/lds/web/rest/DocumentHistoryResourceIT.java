package com.lds.web.rest;

import static com.lds.domain.DocumentHistoryAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.DocumentHistory;
import com.lds.domain.enumeration.DocumentType;
import com.lds.repository.DocumentHistoryRepository;
import com.lds.service.mapper.DocumentHistoryMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link DocumentHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentHistoryResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.RECEIVED_DOCUMENT;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.DOCUMENT_REFERENCE;

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CHANGED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.ofEpochMilli(1786088823278L);

    private static final String DEFAULT_PREVIOUS_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentHistoryMockMvc;

    private DocumentHistory documentHistory;

    private DocumentHistory insertedDocumentHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentHistory createEntity() {
        return new DocumentHistory()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .action(DEFAULT_ACTION)
            .changedBy(DEFAULT_CHANGED_BY)
            .timestamp(DEFAULT_TIMESTAMP)
            .previousValue(DEFAULT_PREVIOUS_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .remarks(DEFAULT_REMARKS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentHistory createUpdatedEntity() {
        return new DocumentHistory()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .action(UPDATED_ACTION)
            .changedBy(UPDATED_CHANGED_BY)
            .timestamp(UPDATED_TIMESTAMP)
            .previousValue(UPDATED_PREVIOUS_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .remarks(UPDATED_REMARKS);
    }

    @BeforeEach
    void initTest() {
        documentHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentHistory != null) {
            documentHistoryRepository.delete(insertedDocumentHistory);
            insertedDocumentHistory = null;
        }
    }

    @Test
    @Transactional
    void getAllDocumentHistories() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].changedBy").value(hasItem(DEFAULT_CHANGED_BY)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].previousValue").value(hasItem(DEFAULT_PREVIOUS_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getDocumentHistory() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get the documentHistory
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, documentHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentHistory.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.changedBy").value(DEFAULT_CHANGED_BY))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.previousValue").value(DEFAULT_PREVIOUS_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getDocumentHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        Long id = documentHistory.getId();

        defaultDocumentHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId equals to
        defaultDocumentHistoryFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId in
        defaultDocumentHistoryFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId is not null
        defaultDocumentHistoryFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId is greater than or equal to
        defaultDocumentHistoryFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId is less than or equal to
        defaultDocumentHistoryFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId is less than
        defaultDocumentHistoryFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentId is greater than
        defaultDocumentHistoryFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentType equals to
        defaultDocumentHistoryFiltering("documentType.equals=" + DEFAULT_DOCUMENT_TYPE, "documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentType in
        defaultDocumentHistoryFiltering(
            "documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE,
            "documentType.in=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where documentType is not null
        defaultDocumentHistoryFiltering("documentType.specified=true", "documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where action equals to
        defaultDocumentHistoryFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where action in
        defaultDocumentHistoryFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where action is not null
        defaultDocumentHistoryFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByActionContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where action contains
        defaultDocumentHistoryFiltering("action.contains=" + DEFAULT_ACTION, "action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByActionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where action does not contain
        defaultDocumentHistoryFiltering("action.doesNotContain=" + UPDATED_ACTION, "action.doesNotContain=" + DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByChangedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where changedBy equals to
        defaultDocumentHistoryFiltering("changedBy.equals=" + DEFAULT_CHANGED_BY, "changedBy.equals=" + UPDATED_CHANGED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByChangedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where changedBy in
        defaultDocumentHistoryFiltering(
            "changedBy.in=" + DEFAULT_CHANGED_BY + "," + UPDATED_CHANGED_BY,
            "changedBy.in=" + UPDATED_CHANGED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByChangedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where changedBy is not null
        defaultDocumentHistoryFiltering("changedBy.specified=true", "changedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByChangedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where changedBy contains
        defaultDocumentHistoryFiltering("changedBy.contains=" + DEFAULT_CHANGED_BY, "changedBy.contains=" + UPDATED_CHANGED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByChangedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where changedBy does not contain
        defaultDocumentHistoryFiltering("changedBy.doesNotContain=" + UPDATED_CHANGED_BY, "changedBy.doesNotContain=" + DEFAULT_CHANGED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where timestamp equals to
        defaultDocumentHistoryFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where timestamp in
        defaultDocumentHistoryFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where timestamp is not null
        defaultDocumentHistoryFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByPreviousValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where previousValue equals to
        defaultDocumentHistoryFiltering("previousValue.equals=" + DEFAULT_PREVIOUS_VALUE, "previousValue.equals=" + UPDATED_PREVIOUS_VALUE);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByPreviousValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where previousValue in
        defaultDocumentHistoryFiltering(
            "previousValue.in=" + DEFAULT_PREVIOUS_VALUE + "," + UPDATED_PREVIOUS_VALUE,
            "previousValue.in=" + UPDATED_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByPreviousValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where previousValue is not null
        defaultDocumentHistoryFiltering("previousValue.specified=true", "previousValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByPreviousValueContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where previousValue contains
        defaultDocumentHistoryFiltering(
            "previousValue.contains=" + DEFAULT_PREVIOUS_VALUE,
            "previousValue.contains=" + UPDATED_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByPreviousValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where previousValue does not contain
        defaultDocumentHistoryFiltering(
            "previousValue.doesNotContain=" + UPDATED_PREVIOUS_VALUE,
            "previousValue.doesNotContain=" + DEFAULT_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByNewValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where newValue equals to
        defaultDocumentHistoryFiltering("newValue.equals=" + DEFAULT_NEW_VALUE, "newValue.equals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByNewValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where newValue in
        defaultDocumentHistoryFiltering("newValue.in=" + DEFAULT_NEW_VALUE + "," + UPDATED_NEW_VALUE, "newValue.in=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByNewValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where newValue is not null
        defaultDocumentHistoryFiltering("newValue.specified=true", "newValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByNewValueContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where newValue contains
        defaultDocumentHistoryFiltering("newValue.contains=" + DEFAULT_NEW_VALUE, "newValue.contains=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllDocumentHistoriesByNewValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentHistory = documentHistoryRepository.saveAndFlush(documentHistory);

        // Get all the documentHistoryList where newValue does not contain
        defaultDocumentHistoryFiltering("newValue.doesNotContain=" + UPDATED_NEW_VALUE, "newValue.doesNotContain=" + DEFAULT_NEW_VALUE);
    }

    private void defaultDocumentHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentHistoryShouldBeFound(shouldBeFound);
        defaultDocumentHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentHistoryShouldBeFound(String filter) throws Exception {
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].changedBy").value(hasItem(DEFAULT_CHANGED_BY)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].previousValue").value(hasItem(DEFAULT_PREVIOUS_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentHistoryShouldNotBeFound(String filter) throws Exception {
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentHistory() throws Exception {
        // Get the documentHistory
        restDocumentHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return documentHistoryRepository.count();
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

    protected DocumentHistory getPersistedDocumentHistory(DocumentHistory documentHistory) {
        return documentHistoryRepository.findById(documentHistory.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentHistoryToMatchAllProperties(DocumentHistory expectedDocumentHistory) {
        assertDocumentHistoryAllPropertiesEquals(expectedDocumentHistory, getPersistedDocumentHistory(expectedDocumentHistory));
    }

    protected void assertPersistedDocumentHistoryToMatchUpdatableProperties(DocumentHistory expectedDocumentHistory) {
        assertDocumentHistoryAllUpdatablePropertiesEquals(expectedDocumentHistory, getPersistedDocumentHistory(expectedDocumentHistory));
    }
}
