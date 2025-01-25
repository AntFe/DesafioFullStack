package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_curso__materia",
        joinColumns = @JoinColumn(name = "curso_id"),
        inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aulas", "professor", "cursos" }, allowSetters = true)
    private Set<Materia> materias = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "endereco", "cursos" }, allowSetters = true)
    private Set<Aluno> alunos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Curso nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Materia> getMaterias() {
        return this.materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }

    public Curso materias(Set<Materia> materias) {
        this.setMaterias(materias);
        return this;
    }

    public Curso addMateria(Materia materia) {
        this.materias.add(materia);
        return this;
    }

    public Curso removeMateria(Materia materia) {
        this.materias.remove(materia);
        return this;
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        if (this.alunos != null) {
            this.alunos.forEach(i -> i.removeCurso(this));
        }
        if (alunos != null) {
            alunos.forEach(i -> i.addCurso(this));
        }
        this.alunos = alunos;
    }

    public Curso alunos(Set<Aluno> alunos) {
        this.setAlunos(alunos);
        return this;
    }

    public Curso addAluno(Aluno aluno) {
        this.alunos.add(aluno);
        aluno.getCursos().add(this);
        return this;
    }

    public Curso removeAluno(Aluno aluno) {
        this.alunos.remove(aluno);
        aluno.getCursos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return getId() != null && getId().equals(((Curso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
