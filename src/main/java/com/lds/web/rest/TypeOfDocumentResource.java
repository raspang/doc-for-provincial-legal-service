package com.lds.web.rest;

import com.lds.repository.TypeOfDocumentRepository;
import com.lds.service.TypeOfDocumentService;
import com.lds.service.dto.TypeOfDocumentDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lds.domain.TypeOfDocument}.
 */
@RestController
@RequestMapping("/api/type-of-documents")
public class TypeOfDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeOfDocumentResource.class);

    private static final String ENTITY_NAME = "typeOfDocument";

    @Value("${jhipster.clientApp.name:legal}")
    private String applicationName;

    private final TypeOfDocumentService typeOfDocumentService;

    private final TypeOfDocumentRepository typeOfDocumentRepository;

    public TypeOfDocumentResource(TypeOfDocumentService typeOfDocumentService, TypeOfDocumentRepository typeOfDocumentRepository) {
        this.typeOfDocumentService = typeOfDocumentService;
        this.typeOfDocumentRepository = typeOfDocumentRepository;
    }

    /**
     * {@code POST  /type-of-documents} : Create a new typeOfDocument.
     *
     * @param typeOfDocumentDTO the typeOfDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOfDocumentDTO, or with status {@code 400 (Bad Request)} if the typeOfDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeOfDocumentDTO> createTypeOfDocument(@Valid @RequestBody TypeOfDocumentDTO typeOfDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TypeOfDocument : {}", typeOfDocumentDTO);
        if (typeOfDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeOfDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeOfDocumentDTO = typeOfDocumentService.save(typeOfDocumentDTO);
        return ResponseEntity.created(new URI("/api/type-of-documents/" + typeOfDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, typeOfDocumentDTO.getId().toString()))
            .body(typeOfDocumentDTO);
    }

    /**
     * {@code PUT  /type-of-documents/:id} : Updates an existing typeOfDocument.
     *
     * @param id the id of the typeOfDocumentDTO to save.
     * @param typeOfDocumentDTO the typeOfDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeOfDocumentDTO> updateTypeOfDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeOfDocumentDTO typeOfDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeOfDocument : {}, {}", id, typeOfDocumentDTO);
        if (typeOfDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeOfDocumentDTO = typeOfDocumentService.update(typeOfDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeOfDocumentDTO.getId().toString()))
            .body(typeOfDocumentDTO);
    }

    /**
     * {@code PATCH  /type-of-documents/:id} : Partial updates given fields of an existing typeOfDocument, field will ignore if it is null
     *
     * @param id the id of the typeOfDocumentDTO to save.
     * @param typeOfDocumentDTO the typeOfDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeOfDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeOfDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeOfDocumentDTO> partialUpdateTypeOfDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeOfDocumentDTO typeOfDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TypeOfDocument partially : {}, {}", id, typeOfDocumentDTO);
        if (typeOfDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeOfDocumentDTO> result = typeOfDocumentService.partialUpdate(typeOfDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeOfDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-of-documents} : get all the Type Of Documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Type Of Documents in body.
     */
    @GetMapping("")
    public List<TypeOfDocumentDTO> getAllTypeOfDocuments() {
        LOG.debug("REST request to get all TypeOfDocuments");
        return typeOfDocumentService.findAll();
    }

    /**
     * {@code GET  /type-of-documents/:id} : get the "id" typeOfDocument.
     *
     * @param id the id of the typeOfDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOfDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeOfDocumentDTO> getTypeOfDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TypeOfDocument : {}", id);
        Optional<TypeOfDocumentDTO> typeOfDocumentDTO = typeOfDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfDocumentDTO);
    }

    /**
     * {@code DELETE  /type-of-documents/:id} : delete the "id" typeOfDocument.
     *
     * @param id the id of the typeOfDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeOfDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TypeOfDocument : {}", id);
        typeOfDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
