package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Local.
 */
@Entity
@Table(name = "local")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Local implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_do_local")
    private String nomeDoLocal;

    @JsonIgnoreProperties(value = { "local", "endereco" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "local")
    private Pais pais;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Local id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoLocal() {
        return this.nomeDoLocal;
    }

    public Local nomeDoLocal(String nomeDoLocal) {
        this.setNomeDoLocal(nomeDoLocal);
        return this;
    }

    public void setNomeDoLocal(String nomeDoLocal) {
        this.nomeDoLocal = nomeDoLocal;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        if (this.pais != null) {
            this.pais.setLocal(null);
        }
        if (pais != null) {
            pais.setLocal(this);
        }
        this.pais = pais;
    }

    public Local pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Local)) {
            return false;
        }
        return getId() != null && getId().equals(((Local) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Local{" +
            "id=" + getId() +
            ", nomeDoLocal='" + getNomeDoLocal() + "'" +
            "}";
    }
}
