package com.lds.config;

import com.lds.service.DocumentDueSoonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DocumentDueSoonScheduler {

    private static final Logger log = LoggerFactory.getLogger(DocumentDueSoonScheduler.class);
    private final DocumentDueSoonService dueSoonService;

    public DocumentDueSoonScheduler(DocumentDueSoonService dueSoonService) {
        this.dueSoonService = dueSoonService;
    }

    /**
     * Runs every day at 8:00 AM server time.
     */
    // @Scheduled(cron = "0 0 8 * * ?")

    // To this (Runs every minute at the 00 second mark):
    // @Scheduled(cron = "0 * * * * ?")

    @Scheduled(cron = "0 0 10 * * ?")
    public void notifyDueSoonDocuments() {
        log.info("Running scheduled due-soon document notification...");
        dueSoonService.findAndNotifyDocumentsDueSoon();
    }
}
