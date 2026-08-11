package com.lds.web.rest;

import com.lds.repository.TransactionTypeRepository;
import com.lds.service.TransactionTypeService;
import com.lds.service.dto.TransactionTypeDTO;
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
 * REST controller for managing {@link com.lds.domain.TransactionType}.
 */
@RestController
@RequestMapping("/api/transaction-types")
public class TransactionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeResource.class);

    private static final String ENTITY_NAME = "transactionType";

    @Value("${jhipster.clientApp.name:legal}")
    private String applicationName;

    private final TransactionTypeService transactionTypeService;

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeResource(TransactionTypeService transactionTypeService, TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeService = transactionTypeService;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * {@code POST  /transaction-types} : Create a new transactionType.
     *
     * @param transactionTypeDTO the transactionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionTypeDTO, or with status {@code 400 (Bad Request)} if the transactionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionTypeDTO> createTransactionType(@Valid @RequestBody TransactionTypeDTO transactionTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TransactionType : {}", transactionTypeDTO);
        if (transactionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionTypeDTO = transactionTypeService.save(transactionTypeDTO);
        return ResponseEntity.created(new URI("/api/transaction-types/" + transactionTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, transactionTypeDTO.getId().toString()))
            .body(transactionTypeDTO);
    }

    /**
     * {@code PUT  /transaction-types/:id} : Updates an existing transactionType.
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionTypeDTO> updateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransactionType : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionTypeDTO = transactionTypeService.update(transactionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionTypeDTO.getId().toString()))
            .body(transactionTypeDTO);
    }

    /**
     * {@code PATCH  /transaction-types/:id} : Partial updates given fields of an existing transactionType, field will ignore if it is null
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionTypeDTO> partialUpdateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransactionType partially : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionTypeDTO> result = transactionTypeService.partialUpdate(transactionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-types} : get all the Transaction Types.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Transaction Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionTypeDTO>> getAllTransactionTypes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TransactionTypes");
        Page<TransactionTypeDTO> page = transactionTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-types/:id} : get the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionTypeDTO> getTransactionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransactionType : {}", id);
        Optional<TransactionTypeDTO> transactionTypeDTO = transactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionTypeDTO);
    }

    /**
     * {@code DELETE  /transaction-types/:id} : delete the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransactionType : {}", id);
        transactionTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
