package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AulaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Aula getAulaSample1() {
        return new Aula().id(1L).tituloAula("tituloAula1").descricao("descricao1").linkVideo("linkVideo1").linkArquivos("linkArquivos1");
    }

    public static Aula getAulaSample2() {
        return new Aula().id(2L).tituloAula("tituloAula2").descricao("descricao2").linkVideo("linkVideo2").linkArquivos("linkArquivos2");
    }

    public static Aula getAulaRandomSampleGenerator() {
        return new Aula()
            .id(longCount.incrementAndGet())
            .tituloAula(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .linkVideo(UUID.randomUUID().toString())
            .linkArquivos(UUID.randomUUID().toString());
    }
}
