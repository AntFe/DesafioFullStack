package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.AulaTestSamples.*;
import static com.plataforma.gtv.domain.MateriaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AulaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aula.class);
        Aula aula1 = getAulaSample1();
        Aula aula2 = new Aula();
        assertThat(aula1).isNotEqualTo(aula2);

        aula2.setId(aula1.getId());
        assertThat(aula1).isEqualTo(aula2);

        aula2 = getAulaSample2();
        assertThat(aula1).isNotEqualTo(aula2);
    }

    @Test
    void materiaTest() {
        Aula aula = getAulaRandomSampleGenerator();
        Materia materiaBack = getMateriaRandomSampleGenerator();

        aula.addMateria(materiaBack);
        assertThat(aula.getMaterias()).containsOnly(materiaBack);
        assertThat(materiaBack.getAulas()).containsOnly(aula);

        aula.removeMateria(materiaBack);
        assertThat(aula.getMaterias()).doesNotContain(materiaBack);
        assertThat(materiaBack.getAulas()).doesNotContain(aula);

        aula.materias(new HashSet<>(Set.of(materiaBack)));
        assertThat(aula.getMaterias()).containsOnly(materiaBack);
        assertThat(materiaBack.getAulas()).containsOnly(aula);

        aula.setMaterias(new HashSet<>());
        assertThat(aula.getMaterias()).doesNotContain(materiaBack);
        assertThat(materiaBack.getAulas()).doesNotContain(aula);
    }
}
