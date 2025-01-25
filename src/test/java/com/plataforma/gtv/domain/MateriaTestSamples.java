package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MateriaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Materia getMateriaSample1() {
        return new Materia().id(1L).nomeDaMateria("nomeDaMateria1");
    }

    public static Materia getMateriaSample2() {
        return new Materia().id(2L).nomeDaMateria("nomeDaMateria2");
    }

    public static Materia getMateriaRandomSampleGenerator() {
        return new Materia().id(longCount.incrementAndGet()).nomeDaMateria(UUID.randomUUID().toString());
    }
}
