package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.RequestedAction} entity.
 */
@Schema(description = "Represents the specific action requested for a document.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestedActionDTO implements Serializable {

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
        if (!(o instanceof RequestedActionDTO)) {
            return false;
        }

        RequestedActionDTO requestedActionDTO = (RequestedActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requestedActionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestedActionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
