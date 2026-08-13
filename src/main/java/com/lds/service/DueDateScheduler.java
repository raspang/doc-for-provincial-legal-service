package com.lds.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DueDateScheduler {

    private final Logger log = LoggerFactory.getLogger(DueDateScheduler.class);
    private final DocumentNotificationService notificationService;

    public DueDateScheduler(DocumentNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Runs every day at 8:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void scheduleDailyWarnings() {
        log.info("Starting daily due date warning check...");
        // notificationService.sendDueDateWarnings();
    }
}
