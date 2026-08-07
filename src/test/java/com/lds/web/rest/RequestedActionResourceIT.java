package com.lds.web.rest;

import static com.lds.domain.RequestedActionAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.RequestedAction;
import com.lds.repository.RequestedActionRepository;
import com.lds.service.dto.RequestedActionDTO;
import com.lds.service.mapper.RequestedActionMapper;
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
 * Integration tests for the {@link RequestedActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestedActionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/requested-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RequestedActionRepository requestedActionRepository;

    @Autowired
    private RequestedActionMapper requestedActionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestedActionMockMvc;

    private RequestedAction requestedAction;

    private RequestedAction insertedRequestedAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestedAction createEntity() {
        return new RequestedAction().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestedAction createUpdatedEntity() {
        return new RequestedAction().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        requestedAction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRequestedAction != null) {
            requestedActionRepository.delete(insertedRequestedAction);
            insertedRequestedAction = null;
        }
    }

    @Test
    @Transactional
    void createRequestedAction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);
        var returnedRequestedActionDTO = om.readValue(
            restRequestedActionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestedActionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RequestedActionDTO.class
        );

        // Validate the RequestedAction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRequestedAction = requestedActionMapper.toEntity(returnedRequestedActionDTO);
        assertRequestedActionUpdatableFieldsEquals(returnedRequestedAction, getPersistedRequestedAction(returnedRequestedAction));

        insertedRequestedAction = returnedRequestedAction;
    }

    @Test
    @Transactional
    void createRequestedActionWithExistingId() throws Exception {
        // Create the RequestedAction with an existing ID
        requestedAction.setId(1L);
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestedActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestedActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        requestedAction.setName(null);

        // Create the RequestedAction, which fails.
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        restRequestedActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestedActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequestedActions() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        // Get all the requestedActionList
        restRequestedActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestedAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRequestedAction() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        // Get the requestedAction
        restRequestedActionMockMvc
            .perform(get(ENTITY_API_URL_ID, requestedAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestedAction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRequestedAction() throws Exception {
        // Get the requestedAction
        restRequestedActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRequestedAction() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestedAction
        RequestedAction updatedRequestedAction = requestedActionRepository.findById(requestedAction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRequestedAction are not directly saved in db
        em.detach(updatedRequestedAction);
        updatedRequestedAction.name(UPDATED_NAME);
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(updatedRequestedAction);

        restRequestedActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestedActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestedActionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRequestedActionToMatchAllProperties(updatedRequestedAction);
    }

    @Test
    @Transactional
    void putNonExistingRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestedActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestedActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestedActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestedActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestedActionWithPatch() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestedAction using partial update
        RequestedAction partialUpdatedRequestedAction = new RequestedAction();
        partialUpdatedRequestedAction.setId(requestedAction.getId());

        partialUpdatedRequestedAction.name(UPDATED_NAME);

        restRequestedActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequestedAction))
            )
            .andExpect(status().isOk());

        // Validate the RequestedAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestedActionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRequestedAction, requestedAction),
            getPersistedRequestedAction(requestedAction)
        );
    }

    @Test
    @Transactional
    void fullUpdateRequestedActionWithPatch() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestedAction using partial update
        RequestedAction partialUpdatedRequestedAction = new RequestedAction();
        partialUpdatedRequestedAction.setId(requestedAction.getId());

        partialUpdatedRequestedAction.name(UPDATED_NAME);

        restRequestedActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequestedAction))
            )
            .andExpect(status().isOk());

        // Validate the RequestedAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestedActionUpdatableFieldsEquals(
            partialUpdatedRequestedAction,
            getPersistedRequestedAction(partialUpdatedRequestedAction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestedActionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestedActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestedActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequestedAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestedAction.setId(longCount.incrementAndGet());

        // Create the RequestedAction
        RequestedActionDTO requestedActionDTO = requestedActionMapper.toDto(requestedAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestedActionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(requestedActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestedAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequestedAction() throws Exception {
        // Initialize the database
        insertedRequestedAction = requestedActionRepository.saveAndFlush(requestedAction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the requestedAction
        restRequestedActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, requestedAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return requestedActionRepository.count();
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

    protected RequestedAction getPersistedRequestedAction(RequestedAction requestedAction) {
        return requestedActionRepository.findById(requestedAction.getId()).orElseThrow();
    }

    protected void assertPersistedRequestedActionToMatchAllProperties(RequestedAction expectedRequestedAction) {
        assertRequestedActionAllPropertiesEquals(expectedRequestedAction, getPersistedRequestedAction(expectedRequestedAction));
    }

    protected void assertPersistedRequestedActionToMatchUpdatableProperties(RequestedAction expectedRequestedAction) {
        assertRequestedActionAllUpdatablePropertiesEquals(expectedRequestedAction, getPersistedRequestedAction(expectedRequestedAction));
    }
}
