package com.lds.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.ResponsiblePerson} entity.
 */
@Schema(description = "Represents the personnel responsible for a document.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponsiblePersonDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String position;

    @NotNull
    private String email;

    private String contactNo;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponsiblePersonDTO)) {
            return false;
        }

        ResponsiblePersonDTO responsiblePersonDTO = (ResponsiblePersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, responsiblePersonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponsiblePersonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", position='" + getPosition() + "'" +
            ", email='" + getEmail() + "'" +
            ", contactNo='" + getContactNo() + "'" +
            "}";
    }
}
