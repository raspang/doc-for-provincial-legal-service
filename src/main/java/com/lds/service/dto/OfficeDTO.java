package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.Office} entity.
 */
@Schema(description = "Represents an office or department within the organization.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfficeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String shortName;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OfficeDTO)) {
            return false;
        }

        OfficeDTO officeDTO = (OfficeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, officeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfficeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            "}";
    }
}
