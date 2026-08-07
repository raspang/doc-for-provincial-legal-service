package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OfficeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Office getOfficeSample1() {
        return new Office().id(1L).name("name1").shortName("shortName1");
    }

    public static Office getOfficeSample2() {
        return new Office().id(2L).name("name2").shortName("shortName2");
    }

    public static Office getOfficeRandomSampleGenerator() {
        return new Office().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}
