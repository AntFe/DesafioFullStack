package com.plataforma.gtv.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PaisAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaisAllPropertiesEquals(Pais expected, Pais actual) {
        assertPaisAutoGeneratedPropertiesEquals(expected, actual);
        assertPaisAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaisAllUpdatablePropertiesEquals(Pais expected, Pais actual) {
        assertPaisUpdatableFieldsEquals(expected, actual);
        assertPaisUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaisAutoGeneratedPropertiesEquals(Pais expected, Pais actual) {
        assertThat(expected)
            .as("Verify Pais auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaisUpdatableFieldsEquals(Pais expected, Pais actual) {
        assertThat(expected)
            .as("Verify Pais relevant properties")
            .satisfies(e -> assertThat(e.getNomeDoPais()).as("check nomeDoPais").isEqualTo(actual.getNomeDoPais()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaisUpdatableRelationshipsEquals(Pais expected, Pais actual) {
        assertThat(expected)
            .as("Verify Pais relationships")
            .satisfies(e -> assertThat(e.getLocal()).as("check local").isEqualTo(actual.getLocal()));
    }
}
