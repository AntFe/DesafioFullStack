package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.AlunoTestSamples.*;
import static com.plataforma.gtv.domain.EnderecoTestSamples.*;
import static com.plataforma.gtv.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endereco.class);
        Endereco endereco1 = getEnderecoSample1();
        Endereco endereco2 = new Endereco();
        assertThat(endereco1).isNotEqualTo(endereco2);

        endereco2.setId(endereco1.getId());
        assertThat(endereco1).isEqualTo(endereco2);

        endereco2 = getEnderecoSample2();
        assertThat(endereco1).isNotEqualTo(endereco2);
    }

    @Test
    void paisTest() {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        endereco.setPais(paisBack);
        assertThat(endereco.getPais()).isEqualTo(paisBack);

        endereco.pais(null);
        assertThat(endereco.getPais()).isNull();
    }

    @Test
    void alunoTest() {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        endereco.setAluno(alunoBack);
        assertThat(endereco.getAluno()).isEqualTo(alunoBack);
        assertThat(alunoBack.getEndereco()).isEqualTo(endereco);

        endereco.aluno(null);
        assertThat(endereco.getAluno()).isNull();
        assertThat(alunoBack.getEndereco()).isNull();
    }
}
