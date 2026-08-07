package com.lds.web.rest;

import com.lds.repository.RequestedActionRepository;
import com.lds.service.RequestedActionService;
import com.lds.service.dto.RequestedActionDTO;
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
 * REST controller for managing {@link com.lds.domain.RequestedAction}.
 */
@RestController
@RequestMapping("/api/requested-actions")
public class RequestedActionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RequestedActionResource.class);

    private static final String ENTITY_NAME = "requestedAction";

    @Value("${jhipster.clientApp.name:legal}")
    private String applicationName;

    private final RequestedActionService requestedActionService;

    private final RequestedActionRepository requestedActionRepository;

    public RequestedActionResource(RequestedActionService requestedActionService, RequestedActionRepository requestedActionRepository) {
        this.requestedActionService = requestedActionService;
        this.requestedActionRepository = requestedActionRepository;
    }

    /**
     * {@code POST  /requested-actions} : Create a new requestedAction.
     *
     * @param requestedActionDTO the requestedActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestedActionDTO, or with status {@code 400 (Bad Request)} if the requestedAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RequestedActionDTO> createRequestedAction(@Valid @RequestBody RequestedActionDTO requestedActionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RequestedAction : {}", requestedActionDTO);
        if (requestedActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestedAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requestedActionDTO = requestedActionService.save(requestedActionDTO);
        return ResponseEntity.created(new URI("/api/requested-actions/" + requestedActionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, requestedActionDTO.getId().toString()))
            .body(requestedActionDTO);
    }

    /**
     * {@code PUT  /requested-actions/:id} : Updates an existing requestedAction.
     *
     * @param id the id of the requestedActionDTO to save.
     * @param requestedActionDTO the requestedActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestedActionDTO,
     * or with status {@code 400 (Bad Request)} if the requestedActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestedActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequestedActionDTO> updateRequestedAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RequestedActionDTO requestedActionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RequestedAction : {}, {}", id, requestedActionDTO);
        if (requestedActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestedActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestedActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        requestedActionDTO = requestedActionService.update(requestedActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requestedActionDTO.getId().toString()))
            .body(requestedActionDTO);
    }

    /**
     * {@code PATCH  /requested-actions/:id} : Partial updates given fields of an existing requestedAction, field will ignore if it is null
     *
     * @param id the id of the requestedActionDTO to save.
     * @param requestedActionDTO the requestedActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestedActionDTO,
     * or with status {@code 400 (Bad Request)} if the requestedActionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestedActionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestedActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RequestedActionDTO> partialUpdateRequestedAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RequestedActionDTO requestedActionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RequestedAction partially : {}, {}", id, requestedActionDTO);
        if (requestedActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestedActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestedActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequestedActionDTO> result = requestedActionService.partialUpdate(requestedActionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requestedActionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /requested-actions} : get all the Requested Actions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Requested Actions in body.
     */
    @GetMapping("")
    public List<RequestedActionDTO> getAllRequestedActions() {
        LOG.debug("REST request to get all RequestedActions");
        return requestedActionService.findAll();
    }

    /**
     * {@code GET  /requested-actions/:id} : get the "id" requestedAction.
     *
     * @param id the id of the requestedActionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestedActionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequestedActionDTO> getRequestedAction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RequestedAction : {}", id);
        Optional<RequestedActionDTO> requestedActionDTO = requestedActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestedActionDTO);
    }

    /**
     * {@code DELETE  /requested-actions/:id} : delete the "id" requestedAction.
     *
     * @param id the id of the requestedActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestedAction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RequestedAction : {}", id);
        requestedActionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
