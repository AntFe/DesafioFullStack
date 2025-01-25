package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnderecoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Endereco getEnderecoSample1() {
        return new Endereco().id(1L).rua("rua1").cep("cep1").cidade("cidade1").estado("estado1");
    }

    public static Endereco getEnderecoSample2() {
        return new Endereco().id(2L).rua("rua2").cep("cep2").cidade("cidade2").estado("estado2");
    }

    public static Endereco getEnderecoRandomSampleGenerator() {
        return new Endereco()
            .id(longCount.incrementAndGet())
            .rua(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString());
    }
}
