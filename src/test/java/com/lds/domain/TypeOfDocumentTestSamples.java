package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeOfDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TypeOfDocument getTypeOfDocumentSample1() {
        return new TypeOfDocument().id(1L).name("name1");
    }

    public static TypeOfDocument getTypeOfDocumentSample2() {
        return new TypeOfDocument().id(2L).name("name2");
    }

    public static TypeOfDocument getTypeOfDocumentRandomSampleGenerator() {
        return new TypeOfDocument().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
