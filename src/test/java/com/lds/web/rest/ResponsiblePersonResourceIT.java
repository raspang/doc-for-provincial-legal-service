package com.lds.web.rest;

import static com.lds.domain.ResponsiblePersonAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.ResponsiblePerson;
import com.lds.repository.ResponsiblePersonRepository;
import com.lds.service.dto.ResponsiblePersonDTO;
import com.lds.service.mapper.ResponsiblePersonMapper;
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
 * Integration tests for the {@link ResponsiblePersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResponsiblePersonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsible-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResponsiblePersonRepository responsiblePersonRepository;

    @Autowired
    private ResponsiblePersonMapper responsiblePersonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponsiblePersonMockMvc;

    private ResponsiblePerson responsiblePerson;

    private ResponsiblePerson insertedResponsiblePerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsiblePerson createEntity() {
        return new ResponsiblePerson().name(DEFAULT_NAME).position(DEFAULT_POSITION).email(DEFAULT_EMAIL).contactNo(DEFAULT_CONTACT_NO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsiblePerson createUpdatedEntity() {
        return new ResponsiblePerson().name(UPDATED_NAME).position(UPDATED_POSITION).email(UPDATED_EMAIL).contactNo(UPDATED_CONTACT_NO);
    }

    @BeforeEach
    void initTest() {
        responsiblePerson = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedResponsiblePerson != null) {
            responsiblePersonRepository.delete(insertedResponsiblePerson);
            insertedResponsiblePerson = null;
        }
    }

    @Test
    @Transactional
    void createResponsiblePerson() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);
        var returnedResponsiblePersonDTO = om.readValue(
            restResponsiblePersonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsiblePersonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResponsiblePersonDTO.class
        );

        // Validate the ResponsiblePerson in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResponsiblePerson = responsiblePersonMapper.toEntity(returnedResponsiblePersonDTO);
        assertResponsiblePersonUpdatableFieldsEquals(returnedResponsiblePerson, getPersistedResponsiblePerson(returnedResponsiblePerson));

        insertedResponsiblePerson = returnedResponsiblePerson;
    }

    @Test
    @Transactional
    void createResponsiblePersonWithExistingId() throws Exception {
        // Create the ResponsiblePerson with an existing ID
        responsiblePerson.setId(1L);
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsiblePersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsiblePersonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        responsiblePerson.setName(null);

        // Create the ResponsiblePerson, which fails.
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        restResponsiblePersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsiblePersonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        responsiblePerson.setEmail(null);

        // Create the ResponsiblePerson, which fails.
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        restResponsiblePersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsiblePersonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResponsiblePeople() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        // Get all the responsiblePersonList
        restResponsiblePersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsiblePerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO)));
    }

    @Test
    @Transactional
    void getResponsiblePerson() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        // Get the responsiblePerson
        restResponsiblePersonMockMvc
            .perform(get(ENTITY_API_URL_ID, responsiblePerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responsiblePerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO));
    }

    @Test
    @Transactional
    void getNonExistingResponsiblePerson() throws Exception {
        // Get the responsiblePerson
        restResponsiblePersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponsiblePerson() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsiblePerson
        ResponsiblePerson updatedResponsiblePerson = responsiblePersonRepository.findById(responsiblePerson.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResponsiblePerson are not directly saved in db
        em.detach(updatedResponsiblePerson);
        updatedResponsiblePerson.name(UPDATED_NAME).position(UPDATED_POSITION).email(UPDATED_EMAIL).contactNo(UPDATED_CONTACT_NO);
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(updatedResponsiblePerson);

        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsiblePersonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResponsiblePersonToMatchAllProperties(updatedResponsiblePerson);
    }

    @Test
    @Transactional
    void putNonExistingResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsiblePersonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponsiblePersonWithPatch() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsiblePerson using partial update
        ResponsiblePerson partialUpdatedResponsiblePerson = new ResponsiblePerson();
        partialUpdatedResponsiblePerson.setId(responsiblePerson.getId());

        partialUpdatedResponsiblePerson.name(UPDATED_NAME).position(UPDATED_POSITION).email(UPDATED_EMAIL);

        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsiblePerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsiblePerson))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsiblePersonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResponsiblePerson, responsiblePerson),
            getPersistedResponsiblePerson(responsiblePerson)
        );
    }

    @Test
    @Transactional
    void fullUpdateResponsiblePersonWithPatch() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsiblePerson using partial update
        ResponsiblePerson partialUpdatedResponsiblePerson = new ResponsiblePerson();
        partialUpdatedResponsiblePerson.setId(responsiblePerson.getId());

        partialUpdatedResponsiblePerson.name(UPDATED_NAME).position(UPDATED_POSITION).email(UPDATED_EMAIL).contactNo(UPDATED_CONTACT_NO);

        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsiblePerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsiblePerson))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsiblePersonUpdatableFieldsEquals(
            partialUpdatedResponsiblePerson,
            getPersistedResponsiblePerson(partialUpdatedResponsiblePerson)
        );
    }

    @Test
    @Transactional
    void patchNonExistingResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponsiblePerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsiblePerson.setId(longCount.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(responsiblePersonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsiblePerson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponsiblePerson() throws Exception {
        // Initialize the database
        insertedResponsiblePerson = responsiblePersonRepository.saveAndFlush(responsiblePerson);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the responsiblePerson
        restResponsiblePersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, responsiblePerson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return responsiblePersonRepository.count();
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

    protected ResponsiblePerson getPersistedResponsiblePerson(ResponsiblePerson responsiblePerson) {
        return responsiblePersonRepository.findById(responsiblePerson.getId()).orElseThrow();
    }

    protected void assertPersistedResponsiblePersonToMatchAllProperties(ResponsiblePerson expectedResponsiblePerson) {
        assertResponsiblePersonAllPropertiesEquals(expectedResponsiblePerson, getPersistedResponsiblePerson(expectedResponsiblePerson));
    }

    protected void assertPersistedResponsiblePersonToMatchUpdatableProperties(ResponsiblePerson expectedResponsiblePerson) {
        assertResponsiblePersonAllUpdatablePropertiesEquals(
            expectedResponsiblePerson,
            getPersistedResponsiblePerson(expectedResponsiblePerson)
        );
    }
}
