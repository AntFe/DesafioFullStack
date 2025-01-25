package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Servico getServicoSample1() {
        return new Servico().id(1L).nomeDoServico("nomeDoServico1");
    }

    public static Servico getServicoSample2() {
        return new Servico().id(2L).nomeDoServico("nomeDoServico2");
    }

    public static Servico getServicoRandomSampleGenerator() {
        return new Servico().id(longCount.incrementAndGet()).nomeDoServico(UUID.randomUUID().toString());
    }
}
