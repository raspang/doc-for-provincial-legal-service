package com.lds.web.rest;

import static com.lds.domain.TransactionTypeAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.TransactionType;
import com.lds.repository.TransactionTypeRepository;
import com.lds.service.dto.TransactionTypeDTO;
import com.lds.service.mapper.TransactionTypeMapper;
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
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TARGET_DAYS = 1;
    private static final Integer UPDATED_TARGET_DAYS = 2;

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionTypeMapper transactionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    private TransactionType insertedTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity() {
        return new TransactionType().name(DEFAULT_NAME).targetDays(DEFAULT_TARGET_DAYS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity() {
        return new TransactionType().name(UPDATED_NAME).targetDays(UPDATED_TARGET_DAYS);
    }

    @BeforeEach
    void initTest() {
        transactionType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransactionType != null) {
            transactionTypeRepository.delete(insertedTransactionType);
            insertedTransactionType = null;
        }
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);
        var returnedTransactionTypeDTO = om.readValue(
            restTransactionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionTypeDTO.class
        );

        // Validate the TransactionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransactionType = transactionTypeMapper.toEntity(returnedTransactionTypeDTO);
        assertTransactionTypeUpdatableFieldsEquals(returnedTransactionType, getPersistedTransactionType(returnedTransactionType));

        insertedTransactionType = returnedTransactionType;
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionType.setName(null);

        // Create the TransactionType, which fails.
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].targetDays").value(hasItem(DEFAULT_TARGET_DAYS)));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.targetDays").value(DEFAULT_TARGET_DAYS));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.name(UPDATED_NAME).targetDays(UPDATED_TARGET_DAYS);
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(updatedTransactionType);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionTypeToMatchAllProperties(updatedTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.name(UPDATED_NAME).targetDays(UPDATED_TARGET_DAYS);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionType, transactionType),
            getPersistedTransactionType(transactionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.name(UPDATED_NAME).targetDays(UPDATED_TARGET_DAYS);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            partialUpdatedTransactionType,
            getPersistedTransactionType(partialUpdatedTransactionType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDTO transactionTypeDTO = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionTypeRepository.count();
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

    protected TransactionType getPersistedTransactionType(TransactionType transactionType) {
        return transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionTypeToMatchAllProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllPropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }

    protected void assertPersistedTransactionTypeToMatchUpdatableProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllUpdatablePropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }
}
