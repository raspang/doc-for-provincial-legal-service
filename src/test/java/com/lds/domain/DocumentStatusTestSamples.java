package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DocumentStatus getDocumentStatusSample1() {
        return new DocumentStatus().id(1L).name("name1").color("color1");
    }

    public static DocumentStatus getDocumentStatusSample2() {
        return new DocumentStatus().id(2L).name("name2").color("color2");
    }

    public static DocumentStatus getDocumentStatusRandomSampleGenerator() {
        return new DocumentStatus().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).color(UUID.randomUUID().toString());
    }
}
