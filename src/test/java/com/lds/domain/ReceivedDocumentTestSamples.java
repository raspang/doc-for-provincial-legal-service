package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReceivedDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ReceivedDocument getReceivedDocumentSample1() {
        return new ReceivedDocument().id(1L).documentTitle("documentTitle1").days(1).daysBeforeDue(1).remarks("remarks1");
    }

    public static ReceivedDocument getReceivedDocumentSample2() {
        return new ReceivedDocument().id(2L).documentTitle("documentTitle2").days(2).daysBeforeDue(2).remarks("remarks2");
    }

    public static ReceivedDocument getReceivedDocumentRandomSampleGenerator() {
        return new ReceivedDocument()
            .id(longCount.incrementAndGet())
            .documentTitle(UUID.randomUUID().toString())
            .days(intCount.incrementAndGet())
            .daysBeforeDue(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString());
    }
}
