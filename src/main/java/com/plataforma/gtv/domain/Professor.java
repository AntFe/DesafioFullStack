package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "email")
    private String email;

    @Column(name = "numero_telefone")
    private String numeroTelefone;

    @Column(name = "ingresso")
    private Instant ingresso;

    @Column(name = "materia_lecionada")
    private String materiaLecionada;

    @Column(name = "registro_profissional")
    private String registroProfissional;

    @JsonIgnoreProperties(value = { "professor" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Servico servico;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aulas", "professor", "cursos" }, allowSetters = true)
    private Set<Materia> materias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Professor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public Professor sobrenome(String sobrenome) {
        this.setSobrenome(sobrenome);
        return this;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return this.email;
    }

    public Professor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefone() {
        return this.numeroTelefone;
    }

    public Professor numeroTelefone(String numeroTelefone) {
        this.setNumeroTelefone(numeroTelefone);
        return this;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public Instant getIngresso() {
        return this.ingresso;
    }

    public Professor ingresso(Instant ingresso) {
        this.setIngresso(ingresso);
        return this;
    }

    public void setIngresso(Instant ingresso) {
        this.ingresso = ingresso;
    }

    public String getMateriaLecionada() {
        return this.materiaLecionada;
    }

    public Professor materiaLecionada(String materiaLecionada) {
        this.setMateriaLecionada(materiaLecionada);
        return this;
    }

    public void setMateriaLecionada(String materiaLecionada) {
        this.materiaLecionada = materiaLecionada;
    }

    public String getRegistroProfissional() {
        return this.registroProfissional;
    }

    public Professor registroProfissional(String registroProfissional) {
        this.setRegistroProfissional(registroProfissional);
        return this;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public Servico getServico() {
        return this.servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Professor servico(Servico servico) {
        this.setServico(servico);
        return this;
    }

    public Set<Materia> getMaterias() {
        return this.materias;
    }

    public void setMaterias(Set<Materia> materias) {
        if (this.materias != null) {
            this.materias.forEach(i -> i.setProfessor(null));
        }
        if (materias != null) {
            materias.forEach(i -> i.setProfessor(this));
        }
        this.materias = materias;
    }

    public Professor materias(Set<Materia> materias) {
        this.setMaterias(materias);
        return this;
    }

    public Professor addMateria(Materia materia) {
        this.materias.add(materia);
        materia.setProfessor(this);
        return this;
    }

    public Professor removeMateria(Materia materia) {
        this.materias.remove(materia);
        materia.setProfessor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professor)) {
            return false;
        }
        return getId() != null && getId().equals(((Professor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sobrenome='" + getSobrenome() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTelefone='" + getNumeroTelefone() + "'" +
            ", ingresso='" + getIngresso() + "'" +
            ", materiaLecionada='" + getMateriaLecionada() + "'" +
            ", registroProfissional='" + getRegistroProfissional() + "'" +
            "}";
    }
}
