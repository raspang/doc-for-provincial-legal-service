package com.lds.web.rest;

import static com.lds.domain.ReceivedDocumentAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.DocumentStatus;
import com.lds.domain.Office;
import com.lds.domain.ReceivedDocument;
import com.lds.domain.RequestedAction;
import com.lds.domain.ResponsiblePerson;
import com.lds.domain.TransactionType;
import com.lds.domain.TypeOfDocument;
import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.ReceivedDocumentService;
import com.lds.service.dto.ReceivedDocumentDTO;
import com.lds.service.mapper.ReceivedDocumentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReceivedDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReceivedDocumentResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.ofEpochMilli(1786088823278L);

    private static final String DEFAULT_DOCUMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RELEASED = Instant.ofEpochMilli(1786088823278L);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/received-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReceivedDocumentRepository receivedDocumentRepository;

    @Mock
    private ReceivedDocumentRepository receivedDocumentRepositoryMock;

    @Autowired
    private ReceivedDocumentMapper receivedDocumentMapper;

    @Mock
    private ReceivedDocumentService receivedDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceivedDocumentMockMvc;

    private ReceivedDocument receivedDocument;

    private ReceivedDocument insertedReceivedDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceivedDocument createEntity(EntityManager em) {
        ReceivedDocument receivedDocument = new ReceivedDocument()
            .date(DEFAULT_DATE)
            .documentTitle(DEFAULT_DOCUMENT_TITLE)
            .dateReleased(DEFAULT_DATE_RELEASED)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        TypeOfDocument typeOfDocument;
        if (TestUtil.findAll(em, TypeOfDocument.class).isEmpty()) {
            typeOfDocument = TypeOfDocumentResourceIT.createEntity();
            em.persist(typeOfDocument);
            em.flush();
        } else {
            typeOfDocument = TestUtil.findAll(em, TypeOfDocument.class).get(0);
        }
        receivedDocument.setTypeOfDocument(typeOfDocument);
        // Add required entity
        DocumentStatus documentStatus;
        if (TestUtil.findAll(em, DocumentStatus.class).isEmpty()) {
            documentStatus = DocumentStatusResourceIT.createEntity();
            em.persist(documentStatus);
            em.flush();
        } else {
            documentStatus = TestUtil.findAll(em, DocumentStatus.class).get(0);
        }
        receivedDocument.setDocumentStatus(documentStatus);
        return receivedDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceivedDocument createUpdatedEntity(EntityManager em) {
        ReceivedDocument updatedReceivedDocument = new ReceivedDocument()
            .date(UPDATED_DATE)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .dateReleased(UPDATED_DATE_RELEASED)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        TypeOfDocument typeOfDocument;
        if (TestUtil.findAll(em, TypeOfDocument.class).isEmpty()) {
            typeOfDocument = TypeOfDocumentResourceIT.createUpdatedEntity();
            em.persist(typeOfDocument);
            em.flush();
        } else {
            typeOfDocument = TestUtil.findAll(em, TypeOfDocument.class).get(0);
        }
        updatedReceivedDocument.setTypeOfDocument(typeOfDocument);
        // Add required entity
        DocumentStatus documentStatus;
        if (TestUtil.findAll(em, DocumentStatus.class).isEmpty()) {
            documentStatus = DocumentStatusResourceIT.createUpdatedEntity();
            em.persist(documentStatus);
            em.flush();
        } else {
            documentStatus = TestUtil.findAll(em, DocumentStatus.class).get(0);
        }
        updatedReceivedDocument.setDocumentStatus(documentStatus);
        return updatedReceivedDocument;
    }

    @BeforeEach
    void initTest() {
        receivedDocument = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReceivedDocument != null) {
            receivedDocumentRepository.delete(insertedReceivedDocument);
            insertedReceivedDocument = null;
        }
    }

    @Test
    @Transactional
    void createReceivedDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);
        var returnedReceivedDocumentDTO = om.readValue(
            restReceivedDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(receivedDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReceivedDocumentDTO.class
        );

        // Validate the ReceivedDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReceivedDocument = receivedDocumentMapper.toEntity(returnedReceivedDocumentDTO);
        assertReceivedDocumentUpdatableFieldsEquals(returnedReceivedDocument, getPersistedReceivedDocument(returnedReceivedDocument));

        insertedReceivedDocument = returnedReceivedDocument;
    }

    @Test
    @Transactional
    void createReceivedDocumentWithExistingId() throws Exception {
        // Create the ReceivedDocument with an existing ID
        receivedDocument.setId(1L);
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceivedDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(receivedDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        receivedDocument.setDate(null);

        // Create the ReceivedDocument, which fails.
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        restReceivedDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(receivedDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        receivedDocument.setDocumentTitle(null);

        // Create the ReceivedDocument, which fails.
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        restReceivedDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(receivedDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReceivedDocuments() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receivedDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].dateReleased").value(hasItem(DEFAULT_DATE_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceivedDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(receivedDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceivedDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(receivedDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceivedDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(receivedDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceivedDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(receivedDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReceivedDocument() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get the receivedDocument
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, receivedDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receivedDocument.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.documentTitle").value(DEFAULT_DOCUMENT_TITLE))
            .andExpect(jsonPath("$.dateReleased").value(DEFAULT_DATE_RELEASED.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getReceivedDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        Long id = receivedDocument.getId();

        defaultReceivedDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReceivedDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReceivedDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where date equals to
        defaultReceivedDocumentFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where date in
        defaultReceivedDocumentFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where date is not null
        defaultReceivedDocumentFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where documentTitle equals to
        defaultReceivedDocumentFiltering(
            "documentTitle.equals=" + DEFAULT_DOCUMENT_TITLE,
            "documentTitle.equals=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where documentTitle in
        defaultReceivedDocumentFiltering(
            "documentTitle.in=" + DEFAULT_DOCUMENT_TITLE + "," + UPDATED_DOCUMENT_TITLE,
            "documentTitle.in=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where documentTitle is not null
        defaultReceivedDocumentFiltering("documentTitle.specified=true", "documentTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where documentTitle contains
        defaultReceivedDocumentFiltering(
            "documentTitle.contains=" + DEFAULT_DOCUMENT_TITLE,
            "documentTitle.contains=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where documentTitle does not contain
        defaultReceivedDocumentFiltering(
            "documentTitle.doesNotContain=" + UPDATED_DOCUMENT_TITLE,
            "documentTitle.doesNotContain=" + DEFAULT_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateReleasedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where dateReleased equals to
        defaultReceivedDocumentFiltering("dateReleased.equals=" + DEFAULT_DATE_RELEASED, "dateReleased.equals=" + UPDATED_DATE_RELEASED);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateReleasedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where dateReleased in
        defaultReceivedDocumentFiltering(
            "dateReleased.in=" + DEFAULT_DATE_RELEASED + "," + UPDATED_DATE_RELEASED,
            "dateReleased.in=" + UPDATED_DATE_RELEASED
        );
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDateReleasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where dateReleased is not null
        defaultReceivedDocumentFiltering("dateReleased.specified=true", "dateReleased.specified=false");
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where remarks equals to
        defaultReceivedDocumentFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where remarks in
        defaultReceivedDocumentFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where remarks is not null
        defaultReceivedDocumentFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where remarks contains
        defaultReceivedDocumentFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        // Get all the receivedDocumentList where remarks does not contain
        defaultReceivedDocumentFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByRequestedActionIsEqualToSomething() throws Exception {
        RequestedAction requestedAction;
        if (TestUtil.findAll(em, RequestedAction.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            requestedAction = RequestedActionResourceIT.createEntity();
        } else {
            requestedAction = TestUtil.findAll(em, RequestedAction.class).get(0);
        }
        em.persist(requestedAction);
        em.flush();
        receivedDocument.setRequestedAction(requestedAction);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long requestedActionId = requestedAction.getId();
        // Get all the receivedDocumentList where requestedAction equals to requestedActionId
        defaultReceivedDocumentShouldBeFound("requestedActionId.equals=" + requestedActionId);

        // Get all the receivedDocumentList where requestedAction equals to (requestedActionId + 1)
        defaultReceivedDocumentShouldNotBeFound("requestedActionId.equals=" + (requestedActionId + 1));
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByTypeOfDocumentIsEqualToSomething() throws Exception {
        TypeOfDocument typeOfDocument;
        if (TestUtil.findAll(em, TypeOfDocument.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            typeOfDocument = TypeOfDocumentResourceIT.createEntity();
        } else {
            typeOfDocument = TestUtil.findAll(em, TypeOfDocument.class).get(0);
        }
        em.persist(typeOfDocument);
        em.flush();
        receivedDocument.setTypeOfDocument(typeOfDocument);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long typeOfDocumentId = typeOfDocument.getId();
        // Get all the receivedDocumentList where typeOfDocument equals to typeOfDocumentId
        defaultReceivedDocumentShouldBeFound("typeOfDocumentId.equals=" + typeOfDocumentId);

        // Get all the receivedDocumentList where typeOfDocument equals to (typeOfDocumentId + 1)
        defaultReceivedDocumentShouldNotBeFound("typeOfDocumentId.equals=" + (typeOfDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByOfficeIsEqualToSomething() throws Exception {
        Office office;
        if (TestUtil.findAll(em, Office.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            office = OfficeResourceIT.createEntity();
        } else {
            office = TestUtil.findAll(em, Office.class).get(0);
        }
        em.persist(office);
        em.flush();
        receivedDocument.setOffice(office);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long officeId = office.getId();
        // Get all the receivedDocumentList where office equals to officeId
        defaultReceivedDocumentShouldBeFound("officeId.equals=" + officeId);

        // Get all the receivedDocumentList where office equals to (officeId + 1)
        defaultReceivedDocumentShouldNotBeFound("officeId.equals=" + (officeId + 1));
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByResponsiblePersonIsEqualToSomething() throws Exception {
        ResponsiblePerson responsiblePerson;
        if (TestUtil.findAll(em, ResponsiblePerson.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            responsiblePerson = ResponsiblePersonResourceIT.createEntity();
        } else {
            responsiblePerson = TestUtil.findAll(em, ResponsiblePerson.class).get(0);
        }
        em.persist(responsiblePerson);
        em.flush();
        receivedDocument.setResponsiblePerson(responsiblePerson);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long responsiblePersonId = responsiblePerson.getId();
        // Get all the receivedDocumentList where responsiblePerson equals to responsiblePersonId
        defaultReceivedDocumentShouldBeFound("responsiblePersonId.equals=" + responsiblePersonId);

        // Get all the receivedDocumentList where responsiblePerson equals to (responsiblePersonId + 1)
        defaultReceivedDocumentShouldNotBeFound("responsiblePersonId.equals=" + (responsiblePersonId + 1));
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByDocumentStatusIsEqualToSomething() throws Exception {
        DocumentStatus documentStatus;
        if (TestUtil.findAll(em, DocumentStatus.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            documentStatus = DocumentStatusResourceIT.createEntity();
        } else {
            documentStatus = TestUtil.findAll(em, DocumentStatus.class).get(0);
        }
        em.persist(documentStatus);
        em.flush();
        receivedDocument.setDocumentStatus(documentStatus);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long documentStatusId = documentStatus.getId();
        // Get all the receivedDocumentList where documentStatus equals to documentStatusId
        defaultReceivedDocumentShouldBeFound("documentStatusId.equals=" + documentStatusId);

        // Get all the receivedDocumentList where documentStatus equals to (documentStatusId + 1)
        defaultReceivedDocumentShouldNotBeFound("documentStatusId.equals=" + (documentStatusId + 1));
    }

    @Test
    @Transactional
    void getAllReceivedDocumentsByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            receivedDocumentRepository.saveAndFlush(receivedDocument);
            transactionType = TransactionTypeResourceIT.createEntity();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        receivedDocument.setTransactionType(transactionType);
        receivedDocumentRepository.saveAndFlush(receivedDocument);
        Long transactionTypeId = transactionType.getId();
        // Get all the receivedDocumentList where transactionType equals to transactionTypeId
        defaultReceivedDocumentShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the receivedDocumentList where transactionType equals to (transactionTypeId + 1)
        defaultReceivedDocumentShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    private void defaultReceivedDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReceivedDocumentShouldBeFound(shouldBeFound);
        defaultReceivedDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReceivedDocumentShouldBeFound(String filter) throws Exception {
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receivedDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].dateReleased").value(hasItem(DEFAULT_DATE_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReceivedDocumentShouldNotBeFound(String filter) throws Exception {
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReceivedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReceivedDocument() throws Exception {
        // Get the receivedDocument
        restReceivedDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReceivedDocument() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receivedDocument
        ReceivedDocument updatedReceivedDocument = receivedDocumentRepository.findById(receivedDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReceivedDocument are not directly saved in db
        em.detach(updatedReceivedDocument);
        updatedReceivedDocument
            .date(UPDATED_DATE)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .dateReleased(UPDATED_DATE_RELEASED)
            .remarks(UPDATED_REMARKS);
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(updatedReceivedDocument);

        restReceivedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receivedDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(receivedDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReceivedDocumentToMatchAllProperties(updatedReceivedDocument);
    }

    @Test
    @Transactional
    void putNonExistingReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receivedDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(receivedDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(receivedDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(receivedDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceivedDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receivedDocument using partial update
        ReceivedDocument partialUpdatedReceivedDocument = new ReceivedDocument();
        partialUpdatedReceivedDocument.setId(receivedDocument.getId());

        restReceivedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceivedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReceivedDocument))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReceivedDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReceivedDocument, receivedDocument),
            getPersistedReceivedDocument(receivedDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateReceivedDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receivedDocument using partial update
        ReceivedDocument partialUpdatedReceivedDocument = new ReceivedDocument();
        partialUpdatedReceivedDocument.setId(receivedDocument.getId());

        partialUpdatedReceivedDocument
            .date(UPDATED_DATE)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .dateReleased(UPDATED_DATE_RELEASED)
            .remarks(UPDATED_REMARKS);

        restReceivedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceivedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReceivedDocument))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReceivedDocumentUpdatableFieldsEquals(
            partialUpdatedReceivedDocument,
            getPersistedReceivedDocument(partialUpdatedReceivedDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receivedDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(receivedDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(receivedDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceivedDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receivedDocument.setId(longCount.incrementAndGet());

        // Create the ReceivedDocument
        ReceivedDocumentDTO receivedDocumentDTO = receivedDocumentMapper.toDto(receivedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(receivedDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceivedDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceivedDocument() throws Exception {
        // Initialize the database
        insertedReceivedDocument = receivedDocumentRepository.saveAndFlush(receivedDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the receivedDocument
        restReceivedDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, receivedDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return receivedDocumentRepository.count();
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

    protected ReceivedDocument getPersistedReceivedDocument(ReceivedDocument receivedDocument) {
        return receivedDocumentRepository.findById(receivedDocument.getId()).orElseThrow();
    }

    protected void assertPersistedReceivedDocumentToMatchAllProperties(ReceivedDocument expectedReceivedDocument) {
        assertReceivedDocumentAllPropertiesEquals(expectedReceivedDocument, getPersistedReceivedDocument(expectedReceivedDocument));
    }

    protected void assertPersistedReceivedDocumentToMatchUpdatableProperties(ReceivedDocument expectedReceivedDocument) {
        assertReceivedDocumentAllUpdatablePropertiesEquals(
            expectedReceivedDocument,
            getPersistedReceivedDocument(expectedReceivedDocument)
        );
    }
}
