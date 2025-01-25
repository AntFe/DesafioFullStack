package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.AlunoTestSamples.*;
import static com.plataforma.gtv.domain.CursoTestSamples.*;
import static com.plataforma.gtv.domain.MateriaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CursoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Curso.class);
        Curso curso1 = getCursoSample1();
        Curso curso2 = new Curso();
        assertThat(curso1).isNotEqualTo(curso2);

        curso2.setId(curso1.getId());
        assertThat(curso1).isEqualTo(curso2);

        curso2 = getCursoSample2();
        assertThat(curso1).isNotEqualTo(curso2);
    }

    @Test
    void materiaTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Materia materiaBack = getMateriaRandomSampleGenerator();

        curso.addMateria(materiaBack);
        assertThat(curso.getMaterias()).containsOnly(materiaBack);

        curso.removeMateria(materiaBack);
        assertThat(curso.getMaterias()).doesNotContain(materiaBack);

        curso.materias(new HashSet<>(Set.of(materiaBack)));
        assertThat(curso.getMaterias()).containsOnly(materiaBack);

        curso.setMaterias(new HashSet<>());
        assertThat(curso.getMaterias()).doesNotContain(materiaBack);
    }

    @Test
    void alunoTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        curso.addAluno(alunoBack);
        assertThat(curso.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getCursos()).containsOnly(curso);

        curso.removeAluno(alunoBack);
        assertThat(curso.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getCursos()).doesNotContain(curso);

        curso.alunos(new HashSet<>(Set.of(alunoBack)));
        assertThat(curso.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getCursos()).containsOnly(curso);

        curso.setAlunos(new HashSet<>());
        assertThat(curso.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getCursos()).doesNotContain(curso);
    }
}
