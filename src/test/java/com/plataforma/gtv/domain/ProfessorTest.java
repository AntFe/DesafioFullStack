package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.MateriaTestSamples.*;
import static com.plataforma.gtv.domain.ProfessorTestSamples.*;
import static com.plataforma.gtv.domain.ServicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfessorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professor.class);
        Professor professor1 = getProfessorSample1();
        Professor professor2 = new Professor();
        assertThat(professor1).isNotEqualTo(professor2);

        professor2.setId(professor1.getId());
        assertThat(professor1).isEqualTo(professor2);

        professor2 = getProfessorSample2();
        assertThat(professor1).isNotEqualTo(professor2);
    }

    @Test
    void servicoTest() {
        Professor professor = getProfessorRandomSampleGenerator();
        Servico servicoBack = getServicoRandomSampleGenerator();

        professor.setServico(servicoBack);
        assertThat(professor.getServico()).isEqualTo(servicoBack);

        professor.servico(null);
        assertThat(professor.getServico()).isNull();
    }

    @Test
    void materiaTest() {
        Professor professor = getProfessorRandomSampleGenerator();
        Materia materiaBack = getMateriaRandomSampleGenerator();

        professor.addMateria(materiaBack);
        assertThat(professor.getMaterias()).containsOnly(materiaBack);
        assertThat(materiaBack.getProfessor()).isEqualTo(professor);

        professor.removeMateria(materiaBack);
        assertThat(professor.getMaterias()).doesNotContain(materiaBack);
        assertThat(materiaBack.getProfessor()).isNull();

        professor.materias(new HashSet<>(Set.of(materiaBack)));
        assertThat(professor.getMaterias()).containsOnly(materiaBack);
        assertThat(materiaBack.getProfessor()).isEqualTo(professor);

        professor.setMaterias(new HashSet<>());
        assertThat(professor.getMaterias()).doesNotContain(materiaBack);
        assertThat(materiaBack.getProfessor()).isNull();
    }
}
