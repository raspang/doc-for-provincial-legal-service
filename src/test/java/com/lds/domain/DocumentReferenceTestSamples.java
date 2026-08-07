package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentReferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DocumentReference getDocumentReferenceSample1() {
        return new DocumentReference()
            .id(1L)
            .referenceNo("referenceNo1")
            .documentTitle("documentTitle1")
            .author("author1")
            .remarks("remarks1");
    }

    public static DocumentReference getDocumentReferenceSample2() {
        return new DocumentReference()
            .id(2L)
            .referenceNo("referenceNo2")
            .documentTitle("documentTitle2")
            .author("author2")
            .remarks("remarks2");
    }

    public static DocumentReference getDocumentReferenceRandomSampleGenerator() {
        return new DocumentReference()
            .id(longCount.incrementAndGet())
            .referenceNo(UUID.randomUUID().toString())
            .documentTitle(UUID.randomUUID().toString())
            .author(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
