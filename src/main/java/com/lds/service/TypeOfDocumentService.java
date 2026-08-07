package com.lds.service;

import com.lds.domain.TypeOfDocument;
import com.lds.repository.TypeOfDocumentRepository;
import com.lds.service.dto.TypeOfDocumentDTO;
import com.lds.service.mapper.TypeOfDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lds.domain.TypeOfDocument}.
 */
@Service
@Transactional
public class TypeOfDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeOfDocumentService.class);

    private final TypeOfDocumentRepository typeOfDocumentRepository;

    private final TypeOfDocumentMapper typeOfDocumentMapper;

    public TypeOfDocumentService(TypeOfDocumentRepository typeOfDocumentRepository, TypeOfDocumentMapper typeOfDocumentMapper) {
        this.typeOfDocumentRepository = typeOfDocumentRepository;
        this.typeOfDocumentMapper = typeOfDocumentMapper;
    }

    /**
     * Save a typeOfDocument.
     *
     * @param typeOfDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeOfDocumentDTO save(TypeOfDocumentDTO typeOfDocumentDTO) {
        LOG.debug("Request to save TypeOfDocument : {}", typeOfDocumentDTO);
        TypeOfDocument typeOfDocument = typeOfDocumentMapper.toEntity(typeOfDocumentDTO);
        typeOfDocument = typeOfDocumentRepository.save(typeOfDocument);
        return typeOfDocumentMapper.toDto(typeOfDocument);
    }

    /**
     * Update a typeOfDocument.
     *
     * @param typeOfDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeOfDocumentDTO update(TypeOfDocumentDTO typeOfDocumentDTO) {
        LOG.debug("Request to update TypeOfDocument : {}", typeOfDocumentDTO);
        TypeOfDocument typeOfDocument = typeOfDocumentMapper.toEntity(typeOfDocumentDTO);
        typeOfDocument = typeOfDocumentRepository.save(typeOfDocument);
        return typeOfDocumentMapper.toDto(typeOfDocument);
    }

    /**
     * Partially update a typeOfDocument.
     *
     * @param typeOfDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeOfDocumentDTO> partialUpdate(TypeOfDocumentDTO typeOfDocumentDTO) {
        LOG.debug("Request to partially update TypeOfDocument : {}", typeOfDocumentDTO);

        return typeOfDocumentRepository
            .findById(typeOfDocumentDTO.getId())
            .map(existingTypeOfDocument -> {
                typeOfDocumentMapper.partialUpdate(existingTypeOfDocument, typeOfDocumentDTO);

                return existingTypeOfDocument;
            })
            .map(typeOfDocumentRepository::save)
            .map(typeOfDocumentMapper::toDto);
    }

    /**
     * Get all the typeOfDocuments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeOfDocumentDTO> findAll() {
        LOG.debug("Request to get all TypeOfDocuments");
        return typeOfDocumentRepository
            .findAll()
            .stream()
            .map(typeOfDocumentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one typeOfDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeOfDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get TypeOfDocument : {}", id);
        return typeOfDocumentRepository.findById(id).map(typeOfDocumentMapper::toDto);
    }

    /**
     * Delete the typeOfDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TypeOfDocument : {}", id);
        typeOfDocumentRepository.deleteById(id);
    }
}
