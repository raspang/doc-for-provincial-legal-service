package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.TypeOfDocument} entity.
 */
@Schema(description = "A type or category for a document, e.g., \"Memorandum\", \"Letter\", \"Certificate\".")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOfDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfDocumentDTO)) {
            return false;
        }

        TypeOfDocumentDTO typeOfDocumentDTO = (TypeOfDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeOfDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOfDocumentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
