package com.lds.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_reference_sequence")
public class DocumentReferenceSequence {

    @Id
    @Column(name = "year_month", length = 7)
    private String yearMonth; // Format: "2026-08"

    @Column(name = "current_sequence")
    private Long currentSequence = 0L;

    // Getters and Setters
    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Long getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Long currentSequence) {
        this.currentSequence = currentSequence;
    }
}
