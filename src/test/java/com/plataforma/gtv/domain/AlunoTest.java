package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.AlunoTestSamples.*;
import static com.plataforma.gtv.domain.CursoTestSamples.*;
import static com.plataforma.gtv.domain.EnderecoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aluno.class);
        Aluno aluno1 = getAlunoSample1();
        Aluno aluno2 = new Aluno();
        assertThat(aluno1).isNotEqualTo(aluno2);

        aluno2.setId(aluno1.getId());
        assertThat(aluno1).isEqualTo(aluno2);

        aluno2 = getAlunoSample2();
        assertThat(aluno1).isNotEqualTo(aluno2);
    }

    @Test
    void enderecoTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        aluno.setEndereco(enderecoBack);
        assertThat(aluno.getEndereco()).isEqualTo(enderecoBack);

        aluno.endereco(null);
        assertThat(aluno.getEndereco()).isNull();
    }

    @Test
    void cursoTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        aluno.addCurso(cursoBack);
        assertThat(aluno.getCursos()).containsOnly(cursoBack);

        aluno.removeCurso(cursoBack);
        assertThat(aluno.getCursos()).doesNotContain(cursoBack);

        aluno.cursos(new HashSet<>(Set.of(cursoBack)));
        assertThat(aluno.getCursos()).containsOnly(cursoBack);

        aluno.setCursos(new HashSet<>());
        assertThat(aluno.getCursos()).doesNotContain(cursoBack);
    }
}
