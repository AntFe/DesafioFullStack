package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CursoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Curso getCursoSample1() {
        return new Curso().id(1L).nome("nome1");
    }

    public static Curso getCursoSample2() {
        return new Curso().id(2L).nome("nome2");
    }

    public static Curso getCursoRandomSampleGenerator() {
        return new Curso().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
