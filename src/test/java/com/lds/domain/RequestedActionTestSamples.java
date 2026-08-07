package com.lds.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RequestedActionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RequestedAction getRequestedActionSample1() {
        return new RequestedAction().id(1L).name("name1");
    }

    public static RequestedAction getRequestedActionSample2() {
        return new RequestedAction().id(2L).name("name2");
    }

    public static RequestedAction getRequestedActionRandomSampleGenerator() {
        return new RequestedAction().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
