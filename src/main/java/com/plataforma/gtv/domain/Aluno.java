package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Aluno
 */
@Schema(description = "Aluno")
@Entity
@Table(name = "aluno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.")
    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "email")
    private String email;

    @Column(name = "numero_telefone")
    private String numeroTelefone;

    @Column(name = "matricula_data")
    private Instant matriculaData;

    @Column(name = "maticula")
    private String maticula;

    @JsonIgnoreProperties(value = { "pais", "aluno" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Endereco endereco;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_aluno__curso", joinColumns = @JoinColumn(name = "aluno_id"), inverseJoinColumns = @JoinColumn(name = "curso_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materias", "alunos" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aluno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Aluno nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public Aluno sobrenome(String sobrenome) {
        this.setSobrenome(sobrenome);
        return this;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return this.email;
    }

    public Aluno email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefone() {
        return this.numeroTelefone;
    }

    public Aluno numeroTelefone(String numeroTelefone) {
        this.setNumeroTelefone(numeroTelefone);
        return this;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public Instant getMatriculaData() {
        return this.matriculaData;
    }

    public Aluno matriculaData(Instant matriculaData) {
        this.setMatriculaData(matriculaData);
        return this;
    }

    public void setMatriculaData(Instant matriculaData) {
        this.matriculaData = matriculaData;
    }

    public String getMaticula() {
        return this.maticula;
    }

    public Aluno maticula(String maticula) {
        this.setMaticula(maticula);
        return this;
    }

    public void setMaticula(String maticula) {
        this.maticula = maticula;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Aluno endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public Aluno cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Aluno addCurso(Curso curso) {
        this.cursos.add(curso);
        return this;
    }

    public Aluno removeCurso(Curso curso) {
        this.cursos.remove(curso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aluno)) {
            return false;
        }
        return getId() != null && getId().equals(((Aluno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aluno{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sobrenome='" + getSobrenome() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTelefone='" + getNumeroTelefone() + "'" +
            ", matriculaData='" + getMatriculaData() + "'" +
            ", maticula='" + getMaticula() + "'" +
            "}";
    }
}
