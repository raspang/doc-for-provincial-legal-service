package com.lds.web.rest;

import static com.lds.domain.DocumentReferenceAsserts.*;
import static com.lds.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.IntegrationTest;
import com.lds.domain.DocumentReference;
import com.lds.domain.TypeOfDocument;
import com.lds.repository.DocumentReferenceRepository;
import com.lds.service.DocumentReferenceService;
import com.lds.service.dto.DocumentReferenceDTO;
import com.lds.service.mapper.DocumentReferenceMapper;
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
 * Integration tests for the {@link DocumentReferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentReferenceResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.ofEpochMilli(1786088823278L);

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RELEASED = Instant.ofEpochMilli(1786088823278L);

    private static final Instant DEFAULT_SUBMITTED_TO_SIR_KING = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_TO_SIR_KING = Instant.ofEpochMilli(1786088823278L);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentReferenceRepository documentReferenceRepository;

    @Mock
    private DocumentReferenceRepository documentReferenceRepositoryMock;

    @Autowired
    private DocumentReferenceMapper documentReferenceMapper;

    @Mock
    private DocumentReferenceService documentReferenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentReferenceMockMvc;

    private DocumentReference documentReference;

    private DocumentReference insertedDocumentReference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReference createEntity(EntityManager em) {
        DocumentReference documentReference = new DocumentReference()
            .date(DEFAULT_DATE)
            .referenceNo(DEFAULT_REFERENCE_NO)
            .documentTitle(DEFAULT_DOCUMENT_TITLE)
            .author(DEFAULT_AUTHOR)
            .dateReleased(DEFAULT_DATE_RELEASED)
            .submittedToSirKing(DEFAULT_SUBMITTED_TO_SIR_KING)
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
        documentReference.setTypeOfDocument(typeOfDocument);
        return documentReference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReference createUpdatedEntity(EntityManager em) {
        DocumentReference updatedDocumentReference = new DocumentReference()
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .author(UPDATED_AUTHOR)
            .dateReleased(UPDATED_DATE_RELEASED)
            .submittedToSirKing(UPDATED_SUBMITTED_TO_SIR_KING)
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
        updatedDocumentReference.setTypeOfDocument(typeOfDocument);
        return updatedDocumentReference;
    }

    @BeforeEach
    void initTest() {
        documentReference = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentReference != null) {
            documentReferenceRepository.delete(insertedDocumentReference);
            insertedDocumentReference = null;
        }
    }

    @Test
    @Transactional
    void createDocumentReference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);
        var returnedDocumentReferenceDTO = om.readValue(
            restDocumentReferenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReferenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentReferenceDTO.class
        );

        // Validate the DocumentReference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentReference = documentReferenceMapper.toEntity(returnedDocumentReferenceDTO);
        assertDocumentReferenceUpdatableFieldsEquals(returnedDocumentReference, getPersistedDocumentReference(returnedDocumentReference));

        insertedDocumentReference = returnedDocumentReference;
    }

    @Test
    @Transactional
    void createDocumentReferenceWithExistingId() throws Exception {
        // Create the DocumentReference with an existing ID
        documentReference.setId(1L);
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentReferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentReference.setDate(null);

        // Create the DocumentReference, which fails.
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        restDocumentReferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentReference.setDocumentTitle(null);

        // Create the DocumentReference, which fails.
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        restDocumentReferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentReferences() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].dateReleased").value(hasItem(DEFAULT_DATE_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].submittedToSirKing").value(hasItem(DEFAULT_SUBMITTED_TO_SIR_KING.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentReferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentReferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentReferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentReferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentReferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentReferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentReferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(documentReferenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocumentReference() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get the documentReference
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, documentReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentReference.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO))
            .andExpect(jsonPath("$.documentTitle").value(DEFAULT_DOCUMENT_TITLE))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.dateReleased").value(DEFAULT_DATE_RELEASED.toString()))
            .andExpect(jsonPath("$.submittedToSirKing").value(DEFAULT_SUBMITTED_TO_SIR_KING.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getDocumentReferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        Long id = documentReference.getId();

        defaultDocumentReferenceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentReferenceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentReferenceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where date equals to
        defaultDocumentReferenceFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where date in
        defaultDocumentReferenceFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where date is not null
        defaultDocumentReferenceFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByReferenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where referenceNo equals to
        defaultDocumentReferenceFiltering("referenceNo.equals=" + DEFAULT_REFERENCE_NO, "referenceNo.equals=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByReferenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where referenceNo in
        defaultDocumentReferenceFiltering(
            "referenceNo.in=" + DEFAULT_REFERENCE_NO + "," + UPDATED_REFERENCE_NO,
            "referenceNo.in=" + UPDATED_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByReferenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where referenceNo is not null
        defaultDocumentReferenceFiltering("referenceNo.specified=true", "referenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByReferenceNoContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where referenceNo contains
        defaultDocumentReferenceFiltering("referenceNo.contains=" + DEFAULT_REFERENCE_NO, "referenceNo.contains=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByReferenceNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where referenceNo does not contain
        defaultDocumentReferenceFiltering(
            "referenceNo.doesNotContain=" + UPDATED_REFERENCE_NO,
            "referenceNo.doesNotContain=" + DEFAULT_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDocumentTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where documentTitle equals to
        defaultDocumentReferenceFiltering(
            "documentTitle.equals=" + DEFAULT_DOCUMENT_TITLE,
            "documentTitle.equals=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDocumentTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where documentTitle in
        defaultDocumentReferenceFiltering(
            "documentTitle.in=" + DEFAULT_DOCUMENT_TITLE + "," + UPDATED_DOCUMENT_TITLE,
            "documentTitle.in=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDocumentTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where documentTitle is not null
        defaultDocumentReferenceFiltering("documentTitle.specified=true", "documentTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDocumentTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where documentTitle contains
        defaultDocumentReferenceFiltering(
            "documentTitle.contains=" + DEFAULT_DOCUMENT_TITLE,
            "documentTitle.contains=" + UPDATED_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDocumentTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where documentTitle does not contain
        defaultDocumentReferenceFiltering(
            "documentTitle.doesNotContain=" + UPDATED_DOCUMENT_TITLE,
            "documentTitle.doesNotContain=" + DEFAULT_DOCUMENT_TITLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where author equals to
        defaultDocumentReferenceFiltering("author.equals=" + DEFAULT_AUTHOR, "author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where author in
        defaultDocumentReferenceFiltering("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR, "author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where author is not null
        defaultDocumentReferenceFiltering("author.specified=true", "author.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByAuthorContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where author contains
        defaultDocumentReferenceFiltering("author.contains=" + DEFAULT_AUTHOR, "author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where author does not contain
        defaultDocumentReferenceFiltering("author.doesNotContain=" + UPDATED_AUTHOR, "author.doesNotContain=" + DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateReleasedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where dateReleased equals to
        defaultDocumentReferenceFiltering("dateReleased.equals=" + DEFAULT_DATE_RELEASED, "dateReleased.equals=" + UPDATED_DATE_RELEASED);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateReleasedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where dateReleased in
        defaultDocumentReferenceFiltering(
            "dateReleased.in=" + DEFAULT_DATE_RELEASED + "," + UPDATED_DATE_RELEASED,
            "dateReleased.in=" + UPDATED_DATE_RELEASED
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByDateReleasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where dateReleased is not null
        defaultDocumentReferenceFiltering("dateReleased.specified=true", "dateReleased.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesBySubmittedToSirKingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where submittedToSirKing equals to
        defaultDocumentReferenceFiltering(
            "submittedToSirKing.equals=" + DEFAULT_SUBMITTED_TO_SIR_KING,
            "submittedToSirKing.equals=" + UPDATED_SUBMITTED_TO_SIR_KING
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesBySubmittedToSirKingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where submittedToSirKing in
        defaultDocumentReferenceFiltering(
            "submittedToSirKing.in=" + DEFAULT_SUBMITTED_TO_SIR_KING + "," + UPDATED_SUBMITTED_TO_SIR_KING,
            "submittedToSirKing.in=" + UPDATED_SUBMITTED_TO_SIR_KING
        );
    }

    @Test
    @Transactional
    void getAllDocumentReferencesBySubmittedToSirKingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where submittedToSirKing is not null
        defaultDocumentReferenceFiltering("submittedToSirKing.specified=true", "submittedToSirKing.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where remarks equals to
        defaultDocumentReferenceFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where remarks in
        defaultDocumentReferenceFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where remarks is not null
        defaultDocumentReferenceFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where remarks contains
        defaultDocumentReferenceFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        // Get all the documentReferenceList where remarks does not contain
        defaultDocumentReferenceFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllDocumentReferencesByTypeOfDocumentIsEqualToSomething() throws Exception {
        TypeOfDocument typeOfDocument;
        if (TestUtil.findAll(em, TypeOfDocument.class).isEmpty()) {
            documentReferenceRepository.saveAndFlush(documentReference);
            typeOfDocument = TypeOfDocumentResourceIT.createEntity();
        } else {
            typeOfDocument = TestUtil.findAll(em, TypeOfDocument.class).get(0);
        }
        em.persist(typeOfDocument);
        em.flush();
        documentReference.setTypeOfDocument(typeOfDocument);
        documentReferenceRepository.saveAndFlush(documentReference);
        Long typeOfDocumentId = typeOfDocument.getId();
        // Get all the documentReferenceList where typeOfDocument equals to typeOfDocumentId
        defaultDocumentReferenceShouldBeFound("typeOfDocumentId.equals=" + typeOfDocumentId);

        // Get all the documentReferenceList where typeOfDocument equals to (typeOfDocumentId + 1)
        defaultDocumentReferenceShouldNotBeFound("typeOfDocumentId.equals=" + (typeOfDocumentId + 1));
    }

    private void defaultDocumentReferenceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentReferenceShouldBeFound(shouldBeFound);
        defaultDocumentReferenceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentReferenceShouldBeFound(String filter) throws Exception {
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].dateReleased").value(hasItem(DEFAULT_DATE_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].submittedToSirKing").value(hasItem(DEFAULT_SUBMITTED_TO_SIR_KING.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentReferenceShouldNotBeFound(String filter) throws Exception {
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentReferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentReference() throws Exception {
        // Get the documentReference
        restDocumentReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentReference() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReference
        DocumentReference updatedDocumentReference = documentReferenceRepository.findById(documentReference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentReference are not directly saved in db
        em.detach(updatedDocumentReference);
        updatedDocumentReference
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .author(UPDATED_AUTHOR)
            .dateReleased(UPDATED_DATE_RELEASED)
            .submittedToSirKing(UPDATED_SUBMITTED_TO_SIR_KING)
            .remarks(UPDATED_REMARKS);
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(updatedDocumentReference);

        restDocumentReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentReferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentReferenceToMatchAllProperties(updatedDocumentReference);
    }

    @Test
    @Transactional
    void putNonExistingDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentReferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentReferenceWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReference using partial update
        DocumentReference partialUpdatedDocumentReference = new DocumentReference();
        partialUpdatedDocumentReference.setId(documentReference.getId());

        partialUpdatedDocumentReference
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .author(UPDATED_AUTHOR);

        restDocumentReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentReference))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentReferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentReference, documentReference),
            getPersistedDocumentReference(documentReference)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentReferenceWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReference using partial update
        DocumentReference partialUpdatedDocumentReference = new DocumentReference();
        partialUpdatedDocumentReference.setId(documentReference.getId());

        partialUpdatedDocumentReference
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .author(UPDATED_AUTHOR)
            .dateReleased(UPDATED_DATE_RELEASED)
            .submittedToSirKing(UPDATED_SUBMITTED_TO_SIR_KING)
            .remarks(UPDATED_REMARKS);

        restDocumentReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentReference))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentReferenceUpdatableFieldsEquals(
            partialUpdatedDocumentReference,
            getPersistedDocumentReference(partialUpdatedDocumentReference)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentReferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReference.setId(longCount.incrementAndGet());

        // Create the DocumentReference
        DocumentReferenceDTO documentReferenceDTO = documentReferenceMapper.toDto(documentReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReferenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentReferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentReference() throws Exception {
        // Initialize the database
        insertedDocumentReference = documentReferenceRepository.saveAndFlush(documentReference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentReference
        restDocumentReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentReference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentReferenceRepository.count();
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

    protected DocumentReference getPersistedDocumentReference(DocumentReference documentReference) {
        return documentReferenceRepository.findById(documentReference.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentReferenceToMatchAllProperties(DocumentReference expectedDocumentReference) {
        assertDocumentReferenceAllPropertiesEquals(expectedDocumentReference, getPersistedDocumentReference(expectedDocumentReference));
    }

    protected void assertPersistedDocumentReferenceToMatchUpdatableProperties(DocumentReference expectedDocumentReference) {
        assertDocumentReferenceAllUpdatablePropertiesEquals(
            expectedDocumentReference,
            getPersistedDocumentReference(expectedDocumentReference)
        );
    }
}
