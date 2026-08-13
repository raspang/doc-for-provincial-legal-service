package com.lds.service;

import com.lds.service.dto.DocumentsDueSoonGroup;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class DocumentNotificationService {

    private static final Logger log = LoggerFactory.getLogger(DocumentNotificationService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from:no-reply@lds.gov.ph}")
    private String fromAddress;

    @Value("${app.notification.due-soon-subject:[ACTION REQUIRED] Documents Due Soon}")
    private String subject;

    public DocumentNotificationService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send a single email to one responsible person with all their due/overdue documents.
     */
    @Async
    public void sendDueSoonNotification(DocumentsDueSoonGroup group) {
        try {
            Context context = new Context(Locale.ENGLISH);
            context.setVariable("personName", group.responsiblePersonName());
            context.setVariable("documents", group.documents());
            context.setVariable("totalDocuments", group.documents().size());

            String htmlContent = templateEngine.process("mail/documentDueSoon", context);

            var message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setFrom(fromAddress);
            helper.setTo(group.email());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.debug("Due-soon email sent to {}", group.email());
        } catch (Exception e) {
            log.error("Could not send due-soon email to {}", group.email(), e);
            throw new RuntimeException("Failed to send email to " + group.email(), e);
        }
    }
}
