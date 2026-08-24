package com.lds.service.dto;

import java.util.List;

public class DashboardDTO {

    private List<CategoryCountDTO> outgoingsByType;
    private List<CategoryCountDTO> incomingsByPerson;
    private List<WeeklyTrendDTO> incomingsWeeklyTrend;
    private String currentMonthLabel; // <--- NEW FIELD

    public List<CategoryCountDTO> getOutgoingsByType() {
        return outgoingsByType;
    }

    public void setOutgoingsByType(List<CategoryCountDTO> outgoingsByType) {
        this.outgoingsByType = outgoingsByType;
    }

    public List<CategoryCountDTO> getIncomingsByPerson() {
        return incomingsByPerson;
    }

    public void setIncomingsByPerson(List<CategoryCountDTO> incomingsByPerson) {
        this.incomingsByPerson = incomingsByPerson;
    }

    public List<WeeklyTrendDTO> getIncomingsWeeklyTrend() {
        return incomingsWeeklyTrend;
    }

    public void setIncomingsWeeklyTrend(List<WeeklyTrendDTO> incomingsWeeklyTrend) {
        this.incomingsWeeklyTrend = incomingsWeeklyTrend;
    }

    public String getCurrentMonthLabel() {
        return currentMonthLabel;
    }

    public void setCurrentMonthLabel(String currentMonthLabel) {
        this.currentMonthLabel = currentMonthLabel;
    }
}
