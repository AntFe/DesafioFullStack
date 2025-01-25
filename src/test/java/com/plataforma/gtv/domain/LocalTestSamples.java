package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Local getLocalSample1() {
        return new Local().id(1L).nomeDoLocal("nomeDoLocal1");
    }

    public static Local getLocalSample2() {
        return new Local().id(2L).nomeDoLocal("nomeDoLocal2");
    }

    public static Local getLocalRandomSampleGenerator() {
        return new Local().id(longCount.incrementAndGet()).nomeDoLocal(UUID.randomUUID().toString());
    }
}
