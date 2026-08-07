package com.lds.web.rest;

import com.lds.service.DocumentHistoryQueryService;
import com.lds.service.DocumentHistoryService;
import com.lds.service.criteria.DocumentHistoryCriteria;
import com.lds.service.dto.DocumentHistoryDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lds.domain.DocumentHistory}.
 */
@RestController
@RequestMapping("/api/document-histories")
public class DocumentHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHistoryResource.class);

    private final DocumentHistoryService documentHistoryService;

    private final DocumentHistoryQueryService documentHistoryQueryService;

    public DocumentHistoryResource(DocumentHistoryService documentHistoryService, DocumentHistoryQueryService documentHistoryQueryService) {
        this.documentHistoryService = documentHistoryService;
        this.documentHistoryQueryService = documentHistoryQueryService;
    }

    /**
     * {@code GET  /document-histories} : get all the Document Histories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Document Histories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentHistoryDTO>> getAllDocumentHistories(
        DocumentHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DocumentHistories by criteria: {}", criteria);

        Page<DocumentHistoryDTO> page = documentHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-histories/count} : count all the documentHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentHistories(DocumentHistoryCriteria criteria) {
        LOG.debug("REST request to count DocumentHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-histories/:id} : get the "id" documentHistory.
     *
     * @param id the id of the documentHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentHistoryDTO> getDocumentHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentHistory : {}", id);
        Optional<DocumentHistoryDTO> documentHistoryDTO = documentHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentHistoryDTO);
    }
}
