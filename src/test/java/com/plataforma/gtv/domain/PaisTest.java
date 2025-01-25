package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.EnderecoTestSamples.*;
import static com.plataforma.gtv.domain.LocalTestSamples.*;
import static com.plataforma.gtv.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pais.class);
        Pais pais1 = getPaisSample1();
        Pais pais2 = new Pais();
        assertThat(pais1).isNotEqualTo(pais2);

        pais2.setId(pais1.getId());
        assertThat(pais1).isEqualTo(pais2);

        pais2 = getPaisSample2();
        assertThat(pais1).isNotEqualTo(pais2);
    }

    @Test
    void localTest() {
        Pais pais = getPaisRandomSampleGenerator();
        Local localBack = getLocalRandomSampleGenerator();

        pais.setLocal(localBack);
        assertThat(pais.getLocal()).isEqualTo(localBack);

        pais.local(null);
        assertThat(pais.getLocal()).isNull();
    }

    @Test
    void enderecoTest() {
        Pais pais = getPaisRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        pais.setEndereco(enderecoBack);
        assertThat(pais.getEndereco()).isEqualTo(enderecoBack);
        assertThat(enderecoBack.getPais()).isEqualTo(pais);

        pais.endereco(null);
        assertThat(pais.getEndereco()).isNull();
        assertThat(enderecoBack.getPais()).isNull();
    }
}
