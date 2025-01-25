package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pais.
 */
@Entity
@Table(name = "pais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_do_pais")
    private String nomeDoPais;

    @JsonIgnoreProperties(value = { "pais" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Local local;

    @JsonIgnoreProperties(value = { "pais", "aluno" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pais")
    private Endereco endereco;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoPais() {
        return this.nomeDoPais;
    }

    public Pais nomeDoPais(String nomeDoPais) {
        this.setNomeDoPais(nomeDoPais);
        return this;
    }

    public void setNomeDoPais(String nomeDoPais) {
        this.nomeDoPais = nomeDoPais;
    }

    public Local getLocal() {
        return this.local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Pais local(Local local) {
        this.setLocal(local);
        return this;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        if (this.endereco != null) {
            this.endereco.setPais(null);
        }
        if (endereco != null) {
            endereco.setPais(this);
        }
        this.endereco = endereco;
    }

    public Pais endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pais)) {
            return false;
        }
        return getId() != null && getId().equals(((Pais) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pais{" +
            "id=" + getId() +
            ", nomeDoPais='" + getNomeDoPais() + "'" +
            "}";
    }
}
