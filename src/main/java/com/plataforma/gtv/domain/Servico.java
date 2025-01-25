package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Servico.
 */
@Entity
@Table(name = "servico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "nome_do_servico")
    private String nomeDoServico;

    @Lob
    @Column(name = "resumo")
    private String resumo;

    @JsonIgnoreProperties(value = { "servico", "materias" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "servico")
    private Professor professor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Servico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Servico startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Servico endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getNomeDoServico() {
        return this.nomeDoServico;
    }

    public Servico nomeDoServico(String nomeDoServico) {
        this.setNomeDoServico(nomeDoServico);
        return this;
    }

    public void setNomeDoServico(String nomeDoServico) {
        this.nomeDoServico = nomeDoServico;
    }

    public String getResumo() {
        return this.resumo;
    }

    public Servico resumo(String resumo) {
        this.setResumo(resumo);
        return this;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        if (this.professor != null) {
            this.professor.setServico(null);
        }
        if (professor != null) {
            professor.setServico(this);
        }
        this.professor = professor;
    }

    public Servico professor(Professor professor) {
        this.setProfessor(professor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servico)) {
            return false;
        }
        return getId() != null && getId().equals(((Servico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Servico{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", nomeDoServico='" + getNomeDoServico() + "'" +
            ", resumo='" + getResumo() + "'" +
            "}";
    }
}
