package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DocumentHistory getDocumentHistorySample1() {
        return new DocumentHistory()
            .id(1L)
            .documentId(1L)
            .action("action1")
            .changedBy("changedBy1")
            .previousValue("previousValue1")
            .newValue("newValue1");
    }

    public static DocumentHistory getDocumentHistorySample2() {
        return new DocumentHistory()
            .id(2L)
            .documentId(2L)
            .action("action2")
            .changedBy("changedBy2")
            .previousValue("previousValue2")
            .newValue("newValue2");
    }

    public static DocumentHistory getDocumentHistoryRandomSampleGenerator() {
        return new DocumentHistory()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .changedBy(UUID.randomUUID().toString())
            .previousValue(UUID.randomUUID().toString())
            .newValue(UUID.randomUUID().toString());
    }
}
