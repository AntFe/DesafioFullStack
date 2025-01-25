package com.plataforma.gtv.domain;

import static com.plataforma.gtv.domain.LocalTestSamples.*;
import static com.plataforma.gtv.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.plataforma.gtv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Local.class);
        Local local1 = getLocalSample1();
        Local local2 = new Local();
        assertThat(local1).isNotEqualTo(local2);

        local2.setId(local1.getId());
        assertThat(local1).isEqualTo(local2);

        local2 = getLocalSample2();
        assertThat(local1).isNotEqualTo(local2);
    }

    @Test
    void paisTest() {
        Local local = getLocalRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        local.setPais(paisBack);
        assertThat(local.getPais()).isEqualTo(paisBack);
        assertThat(paisBack.getLocal()).isEqualTo(local);

        local.pais(null);
        assertThat(local.getPais()).isNull();
        assertThat(paisBack.getLocal()).isNull();
    }
}
