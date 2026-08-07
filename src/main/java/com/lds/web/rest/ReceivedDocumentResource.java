package com.lds.web.rest;

import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.ReceivedDocumentQueryService;
import com.lds.service.ReceivedDocumentService;
import com.lds.service.criteria.ReceivedDocumentCriteria;
import com.lds.service.dto.ReceivedDocumentDTO;
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
 * REST controller for managing {@link com.lds.domain.ReceivedDocument}.
 */
@RestController
@RequestMapping("/api/received-documents")
public class ReceivedDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivedDocumentResource.class);

    private static final String ENTITY_NAME = "receivedDocument";

    @Value("${jhipster.clientApp.name:legal}")
    private String applicationName;

    private final ReceivedDocumentService receivedDocumentService;

    private final ReceivedDocumentRepository receivedDocumentRepository;

    private final ReceivedDocumentQueryService receivedDocumentQueryService;

    public ReceivedDocumentResource(
        ReceivedDocumentService receivedDocumentService,
        ReceivedDocumentRepository receivedDocumentRepository,
        ReceivedDocumentQueryService receivedDocumentQueryService
    ) {
        this.receivedDocumentService = receivedDocumentService;
        this.receivedDocumentRepository = receivedDocumentRepository;
        this.receivedDocumentQueryService = receivedDocumentQueryService;
    }

    /**
     * {@code POST  /received-documents} : Create a new receivedDocument.
     *
     * @param receivedDocumentDTO the receivedDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receivedDocumentDTO, or with status {@code 400 (Bad Request)} if the receivedDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReceivedDocumentDTO> createReceivedDocument(@Valid @RequestBody ReceivedDocumentDTO receivedDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReceivedDocument : {}", receivedDocumentDTO);
        if (receivedDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new receivedDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        receivedDocumentDTO = receivedDocumentService.save(receivedDocumentDTO);
        return ResponseEntity.created(new URI("/api/received-documents/" + receivedDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, receivedDocumentDTO.getId().toString()))
            .body(receivedDocumentDTO);
    }

    /**
     * {@code PUT  /received-documents/:id} : Updates an existing receivedDocument.
     *
     * @param id the id of the receivedDocumentDTO to save.
     * @param receivedDocumentDTO the receivedDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receivedDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the receivedDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receivedDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReceivedDocumentDTO> updateReceivedDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReceivedDocumentDTO receivedDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReceivedDocument : {}, {}", id, receivedDocumentDTO);
        if (receivedDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receivedDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receivedDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        receivedDocumentDTO = receivedDocumentService.update(receivedDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, receivedDocumentDTO.getId().toString()))
            .body(receivedDocumentDTO);
    }

    /**
     * {@code PATCH  /received-documents/:id} : Partial updates given fields of an existing receivedDocument, field will ignore if it is null
     *
     * @param id the id of the receivedDocumentDTO to save.
     * @param receivedDocumentDTO the receivedDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receivedDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the receivedDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receivedDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receivedDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceivedDocumentDTO> partialUpdateReceivedDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReceivedDocumentDTO receivedDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReceivedDocument partially : {}, {}", id, receivedDocumentDTO);
        if (receivedDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receivedDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receivedDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceivedDocumentDTO> result = receivedDocumentService.partialUpdate(receivedDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, receivedDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /received-documents} : get all the Received Documents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Received Documents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReceivedDocumentDTO>> getAllReceivedDocuments(
        ReceivedDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReceivedDocuments by criteria: {}", criteria);

        Page<ReceivedDocumentDTO> page = receivedDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /received-documents/count} : count all the receivedDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReceivedDocuments(ReceivedDocumentCriteria criteria) {
        LOG.debug("REST request to count ReceivedDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(receivedDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /received-documents/:id} : get the "id" receivedDocument.
     *
     * @param id the id of the receivedDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receivedDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReceivedDocumentDTO> getReceivedDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReceivedDocument : {}", id);
        Optional<ReceivedDocumentDTO> receivedDocumentDTO = receivedDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receivedDocumentDTO);
    }

    /**
     * {@code DELETE  /received-documents/:id} : delete the "id" receivedDocument.
     *
     * @param id the id of the receivedDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceivedDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReceivedDocument : {}", id);
        receivedDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
