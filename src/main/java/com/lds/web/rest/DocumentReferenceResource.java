package com.lds.web.rest;

import com.lds.repository.DocumentReferenceRepository;
import com.lds.service.DocumentReferenceQueryService;
import com.lds.service.DocumentReferenceService;
import com.lds.service.criteria.DocumentReferenceCriteria;
import com.lds.service.dto.DocumentReferenceDTO;
import com.lds.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lds.domain.DocumentReference}.
 */
@RestController
@RequestMapping("/api/document-references")
public class DocumentReferenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentReferenceResource.class);

    private static final String ENTITY_NAME = "documentReference";

    @Value("${jhipster.clientApp.name:legal}")
    private String applicationName;

    private final DocumentReferenceService documentReferenceService;

    private final DocumentReferenceRepository documentReferenceRepository;

    private final DocumentReferenceQueryService documentReferenceQueryService;

    public DocumentReferenceResource(
        DocumentReferenceService documentReferenceService,
        DocumentReferenceRepository documentReferenceRepository,
        DocumentReferenceQueryService documentReferenceQueryService
    ) {
        this.documentReferenceService = documentReferenceService;
        this.documentReferenceRepository = documentReferenceRepository;
        this.documentReferenceQueryService = documentReferenceQueryService;
    }

    /**
     * {@code POST  /document-references} : Create a new documentReference.
     *
     * @param documentReferenceDTO the documentReferenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentReferenceDTO, or with status {@code 400 (Bad Request)} if the documentReference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentReferenceDTO> createDocumentReference(@Valid @RequestBody DocumentReferenceDTO documentReferenceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentReference : {}", documentReferenceDTO);
        if (documentReferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentReference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentReferenceDTO = documentReferenceService.save(documentReferenceDTO);
        return ResponseEntity.created(new URI("/api/document-references/" + documentReferenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, documentReferenceDTO.getId().toString()))
            .body(documentReferenceDTO);
    }

    /**
     * {@code PUT  /document-references/:id} : Updates an existing documentReference.
     *
     * @param id the id of the documentReferenceDTO to save.
     * @param documentReferenceDTO the documentReferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReferenceDTO,
     * or with status {@code 400 (Bad Request)} if the documentReferenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentReferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentReferenceDTO> updateDocumentReference(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentReferenceDTO documentReferenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentReference : {}, {}", id, documentReferenceDTO);
        if (documentReferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentReferenceDTO = documentReferenceService.update(documentReferenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentReferenceDTO.getId().toString()))
            .body(documentReferenceDTO);
    }

    /**
     * {@code PATCH  /document-references/:id} : Partial updates given fields of an existing documentReference, field will ignore if it is null
     *
     * @param id the id of the documentReferenceDTO to save.
     * @param documentReferenceDTO the documentReferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReferenceDTO,
     * or with status {@code 400 (Bad Request)} if the documentReferenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentReferenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentReferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentReferenceDTO> partialUpdateDocumentReference(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentReferenceDTO documentReferenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentReference partially : {}, {}", id, documentReferenceDTO);
        if (documentReferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentReferenceDTO> result = documentReferenceService.partialUpdate(documentReferenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentReferenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-references} : get all the Document References.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Document References in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentReferenceDTO>> getAllDocumentReferences(
        DocumentReferenceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentReferences by criteria: {}", criteria);

        Page<DocumentReferenceDTO> page = documentReferenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-references/count} : count all the documentReferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentReferences(DocumentReferenceCriteria criteria) {
        LOG.debug("REST request to count DocumentReferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentReferenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-references/:id} : get the "id" documentReference.
     *
     * @param id the id of the documentReferenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentReferenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentReferenceDTO> getDocumentReference(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentReference : {}", id);
        Optional<DocumentReferenceDTO> documentReferenceDTO = documentReferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentReferenceDTO);
    }

    /**
     * {@code DELETE  /document-references/:id} : delete the "id" documentReference.
     *
     * @param id the id of the documentReferenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentReference(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentReference : {}", id);
        documentReferenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
