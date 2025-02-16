package com.plataforma.gtv.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ServicoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServicoAllPropertiesEquals(Servico expected, Servico actual) {
        assertServicoAutoGeneratedPropertiesEquals(expected, actual);
        assertServicoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServicoAllUpdatablePropertiesEquals(Servico expected, Servico actual) {
        assertServicoUpdatableFieldsEquals(expected, actual);
        assertServicoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServicoAutoGeneratedPropertiesEquals(Servico expected, Servico actual) {
        assertThat(expected)
            .as("Verify Servico auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServicoUpdatableFieldsEquals(Servico expected, Servico actual) {
        assertThat(expected)
            .as("Verify Servico relevant properties")
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getNomeDoServico()).as("check nomeDoServico").isEqualTo(actual.getNomeDoServico()))
            .satisfies(e -> assertThat(e.getResumo()).as("check resumo").isEqualTo(actual.getResumo()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServicoUpdatableRelationshipsEquals(Servico expected, Servico actual) {
        // empty method
    }
}
