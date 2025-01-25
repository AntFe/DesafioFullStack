package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.AulaTestSamples.*;
import static com.plataforma.gtv.domain.CursoTestSamples.*;
import static com.plataforma.gtv.domain.MateriaTestSamples.*;
import static com.plataforma.gtv.domain.ProfessorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MateriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Materia.class);
        Materia materia1 = getMateriaSample1();
        Materia materia2 = new Materia();
        assertThat(materia1).isNotEqualTo(materia2);

        materia2.setId(materia1.getId());
        assertThat(materia1).isEqualTo(materia2);

        materia2 = getMateriaSample2();
        assertThat(materia1).isNotEqualTo(materia2);
    }

    @Test
    void aulaTest() {
        Materia materia = getMateriaRandomSampleGenerator();
        Aula aulaBack = getAulaRandomSampleGenerator();

        materia.addAula(aulaBack);
        assertThat(materia.getAulas()).containsOnly(aulaBack);

        materia.removeAula(aulaBack);
        assertThat(materia.getAulas()).doesNotContain(aulaBack);

        materia.aulas(new HashSet<>(Set.of(aulaBack)));
        assertThat(materia.getAulas()).containsOnly(aulaBack);

        materia.setAulas(new HashSet<>());
        assertThat(materia.getAulas()).doesNotContain(aulaBack);
    }

    @Test
    void professorTest() {
        Materia materia = getMateriaRandomSampleGenerator();
        Professor professorBack = getProfessorRandomSampleGenerator();

        materia.setProfessor(professorBack);
        assertThat(materia.getProfessor()).isEqualTo(professorBack);

        materia.professor(null);
        assertThat(materia.getProfessor()).isNull();
    }

    @Test
    void cursoTest() {
        Materia materia = getMateriaRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        materia.addCurso(cursoBack);
        assertThat(materia.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getMaterias()).containsOnly(materia);

        materia.removeCurso(cursoBack);
        assertThat(materia.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getMaterias()).doesNotContain(materia);

        materia.cursos(new HashSet<>(Set.of(cursoBack)));
        assertThat(materia.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getMaterias()).containsOnly(materia);

        materia.setCursos(new HashSet<>());
        assertThat(materia.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getMaterias()).doesNotContain(materia);
    }
}
