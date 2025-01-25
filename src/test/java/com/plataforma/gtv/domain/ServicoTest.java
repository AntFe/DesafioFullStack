package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.ProfessorTestSamples.*;
import static com.plataforma.gtv.domain.ServicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servico.class);
        Servico servico1 = getServicoSample1();
        Servico servico2 = new Servico();
        assertThat(servico1).isNotEqualTo(servico2);

        servico2.setId(servico1.getId());
        assertThat(servico1).isEqualTo(servico2);

        servico2 = getServicoSample2();
        assertThat(servico1).isNotEqualTo(servico2);
    }

    @Test
    void professorTest() {
        Servico servico = getServicoRandomSampleGenerator();
        Professor professorBack = getProfessorRandomSampleGenerator();

        servico.setProfessor(professorBack);
        assertThat(servico.getProfessor()).isEqualTo(professorBack);
        assertThat(professorBack.getServico()).isEqualTo(servico);

        servico.professor(null);
        assertThat(servico.getProfessor()).isNull();
        assertThat(professorBack.getServico()).isNull();
    }
}
