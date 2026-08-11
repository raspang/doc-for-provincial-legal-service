package com.lds.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lds.domain.TransactionType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer targetDays;

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

    public Integer getTargetDays() {
        return targetDays;
    }

    public void setTargetDays(Integer targetDays) {
        this.targetDays = targetDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionTypeDTO)) {
            return false;
        }

        TransactionTypeDTO transactionTypeDTO = (TransactionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", targetDays=" + getTargetDays() +
            "}";
    }
}
