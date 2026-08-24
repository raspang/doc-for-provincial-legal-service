package com.lds.service;

import com.lds.domain.ReceivedDocument;
import com.lds.repository.DocumentReferenceRepository;
import com.lds.repository.ReceivedDocumentRepository;
import com.lds.service.dto.CategoryCountDTO;
import com.lds.service.dto.DashboardDTO;
import com.lds.service.dto.WeeklyTrendDTO;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final DocumentReferenceRepository documentReferenceRepository;
    private final ReceivedDocumentRepository receivedDocumentRepository;

    public DashboardService(
        DocumentReferenceRepository documentReferenceRepository,
        ReceivedDocumentRepository receivedDocumentRepository
    ) {
        this.documentReferenceRepository = documentReferenceRepository;
        this.receivedDocumentRepository = receivedDocumentRepository;
    }

    public DashboardDTO getDashboardStats() {
        int currentYear = Year.now().getValue();
        DashboardDTO dto = new DashboardDTO();

        // 1. Outgoings by Type (Current Year)
        List<CategoryCountDTO> outgoings = documentReferenceRepository
            .countOutgoingsByType(currentYear)
            .stream()
            .map(obj -> new CategoryCountDTO((String) obj[0], (Long) obj[1]))
            .collect(Collectors.toList());
        dto.setOutgoingsByType(outgoings);

        // 2. Incomings by Responsible Person (Current Year)
        List<CategoryCountDTO> incomingsByPerson = receivedDocumentRepository
            .countIncomingsByPerson(currentYear)
            .stream()
            .map(obj -> new CategoryCountDTO((String) obj[0], (Long) obj[1]))
            .collect(Collectors.toList());
        dto.setIncomingsByPerson(incomingsByPerson);

        // 3. Incomings Current Month Weekly Trend
        YearMonth currentMonth = YearMonth.now();
        int monthValue = currentMonth.getMonthValue();
        String monthLabel = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")); // e.g., "August 2026"
        dto.setCurrentMonthLabel(monthLabel);

        List<ReceivedDocument> yearlyDocs = receivedDocumentRepository.findAllForYear(currentYear);

        // Initialize weeks 1 to 5
        Map<Integer, Long> weekCounts = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) weekCounts.put(i, 0L);

        for (ReceivedDocument rd : yearlyDocs) {
            if (rd.getDate() != null) {
                ZonedDateTime zdt = rd.getDate().atZone(ZoneId.systemDefault());
                // Filter only for the current month
                if (zdt.getMonthValue() == monthValue) {
                    int dayOfMonth = zdt.getDayOfMonth();
                    // Calculate week of month (Days 1-7 = W1, 8-14 = W2, etc.)
                    int weekOfMonth = (dayOfMonth - 1) / 7 + 1;
                    if (weekCounts.containsKey(weekOfMonth)) {
                        weekCounts.put(weekOfMonth, weekCounts.get(weekOfMonth) + 1);
                    }
                }
            }
        }

        List<WeeklyTrendDTO> trend = weekCounts
            .entrySet()
            .stream()
            .map(e -> new WeeklyTrendDTO("Week " + e.getKey(), e.getValue()))
            .collect(Collectors.toList());
        dto.setIncomingsWeeklyTrend(trend);

        return dto;
    }
}
