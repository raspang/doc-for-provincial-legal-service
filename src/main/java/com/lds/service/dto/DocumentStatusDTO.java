package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.DocumentStatus} entity.
 */
@Schema(description = "Defines the status of a document, e.g., \"Pending\", \"Filed\", \"Done\".")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String color;

    private Boolean warning;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentStatusDTO)) {
            return false;
        }

        DocumentStatusDTO documentStatusDTO = (DocumentStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentStatusDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", color='" + getColor() + "'" +
            ", warning='" + getWarning() + "'" +
            "}";
    }
}
