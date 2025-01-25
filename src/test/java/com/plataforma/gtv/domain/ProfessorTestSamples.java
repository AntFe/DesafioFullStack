package com.plataforma.gtv.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Professor getProfessorSample1() {
        return new Professor()
            .id(1L)
            .nome("nome1")
            .sobrenome("sobrenome1")
            .email("email1")
            .numeroTelefone("numeroTelefone1")
            .materiaLecionada("materiaLecionada1")
            .registroProfissional("registroProfissional1");
    }

    public static Professor getProfessorSample2() {
        return new Professor()
            .id(2L)
            .nome("nome2")
            .sobrenome("sobrenome2")
            .email("email2")
            .numeroTelefone("numeroTelefone2")
            .materiaLecionada("materiaLecionada2")
            .registroProfissional("registroProfissional2");
    }

    public static Professor getProfessorRandomSampleGenerator() {
        return new Professor()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .sobrenome(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .numeroTelefone(UUID.randomUUID().toString())
            .materiaLecionada(UUID.randomUUID().toString())
            .registroProfissional(UUID.randomUUID().toString());
    }
}
