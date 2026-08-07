package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResponsiblePersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ResponsiblePerson getResponsiblePersonSample1() {
        return new ResponsiblePerson().id(1L).name("name1").position("position1").email("email1").contactNo("contactNo1");
    }

    public static ResponsiblePerson getResponsiblePersonSample2() {
        return new ResponsiblePerson().id(2L).name("name2").position("position2").email("email2").contactNo("contactNo2");
    }

    public static ResponsiblePerson getResponsiblePersonRandomSampleGenerator() {
        return new ResponsiblePerson()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .contactNo(UUID.randomUUID().toString());
    }
}
