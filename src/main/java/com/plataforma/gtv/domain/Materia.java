package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Materia.
 */
@Entity
@Table(name = "materia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_da_materia")
    private String nomeDaMateria;

    @Lob
    @Column(name = "ementa")
    private String ementa;

    @Lob
    @Column(name = "referencias_bibliograficas")
    private String referenciasBibliograficas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_materia__aula",
        joinColumns = @JoinColumn(name = "materia_id"),
        inverseJoinColumns = @JoinColumn(name = "aula_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materias" }, allowSetters = true)
    private Set<Aula> aulas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servico", "materias" }, allowSetters = true)
    private Professor professor;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "materias")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materias", "alunos" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDaMateria() {
        return this.nomeDaMateria;
    }

    public Materia nomeDaMateria(String nomeDaMateria) {
        this.setNomeDaMateria(nomeDaMateria);
        return this;
    }

    public void setNomeDaMateria(String nomeDaMateria) {
        this.nomeDaMateria = nomeDaMateria;
    }

    public String getEmenta() {
        return this.ementa;
    }

    public Materia ementa(String ementa) {
        this.setEmenta(ementa);
        return this;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public String getReferenciasBibliograficas() {
        return this.referenciasBibliograficas;
    }

    public Materia referenciasBibliograficas(String referenciasBibliograficas) {
        this.setReferenciasBibliograficas(referenciasBibliograficas);
        return this;
    }

    public void setReferenciasBibliograficas(String referenciasBibliograficas) {
        this.referenciasBibliograficas = referenciasBibliograficas;
    }

    public Set<Aula> getAulas() {
        return this.aulas;
    }

    public void setAulas(Set<Aula> aulas) {
        this.aulas = aulas;
    }

    public Materia aulas(Set<Aula> aulas) {
        this.setAulas(aulas);
        return this;
    }

    public Materia addAula(Aula aula) {
        this.aulas.add(aula);
        return this;
    }

    public Materia removeAula(Aula aula) {
        this.aulas.remove(aula);
        return this;
    }

    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Materia professor(Professor professor) {
        this.setProfessor(professor);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        if (this.cursos != null) {
            this.cursos.forEach(i -> i.removeMateria(this));
        }
        if (cursos != null) {
            cursos.forEach(i -> i.addMateria(this));
        }
        this.cursos = cursos;
    }

    public Materia cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Materia addCurso(Curso curso) {
        this.cursos.add(curso);
        curso.getMaterias().add(this);
        return this;
    }

    public Materia removeCurso(Curso curso) {
        this.cursos.remove(curso);
        curso.getMaterias().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materia)) {
            return false;
        }
        return getId() != null && getId().equals(((Materia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materia{" +
            "id=" + getId() +
            ", nomeDaMateria='" + getNomeDaMateria() + "'" +
            ", ementa='" + getEmenta() + "'" +
            ", referenciasBibliograficas='" + getReferenciasBibliograficas() + "'" +
            "}";
    }
}
