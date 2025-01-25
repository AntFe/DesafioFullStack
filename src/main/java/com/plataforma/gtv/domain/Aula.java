package com.plataforma.gtv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aula.
 */
@Entity
@Table(name = "aula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo_aula")
    private String tituloAula;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "link_video")
    private String linkVideo;

    @Column(name = "link_arquivos")
    private String linkArquivos;

    @Lob
    @Column(name = "resumo")
    private String resumo;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "aulas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aulas", "professor", "cursos" }, allowSetters = true)
    private Set<Materia> materias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTituloAula() {
        return this.tituloAula;
    }

    public Aula tituloAula(String tituloAula) {
        this.setTituloAula(tituloAula);
        return this;
    }

    public void setTituloAula(String tituloAula) {
        this.tituloAula = tituloAula;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Aula descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLinkVideo() {
        return this.linkVideo;
    }

    public Aula linkVideo(String linkVideo) {
        this.setLinkVideo(linkVideo);
        return this;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public String getLinkArquivos() {
        return this.linkArquivos;
    }

    public Aula linkArquivos(String linkArquivos) {
        this.setLinkArquivos(linkArquivos);
        return this;
    }

    public void setLinkArquivos(String linkArquivos) {
        this.linkArquivos = linkArquivos;
    }

    public String getResumo() {
        return this.resumo;
    }

    public Aula resumo(String resumo) {
        this.setResumo(resumo);
        return this;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Set<Materia> getMaterias() {
        return this.materias;
    }

    public void setMaterias(Set<Materia> materias) {
        if (this.materias != null) {
            this.materias.forEach(i -> i.removeAula(this));
        }
        if (materias != null) {
            materias.forEach(i -> i.addAula(this));
        }
        this.materias = materias;
    }

    public Aula materias(Set<Materia> materias) {
        this.setMaterias(materias);
        return this;
    }

    public Aula addMateria(Materia materia) {
        this.materias.add(materia);
        materia.getAulas().add(this);
        return this;
    }

    public Aula removeMateria(Materia materia) {
        this.materias.remove(materia);
        materia.getAulas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aula)) {
            return false;
        }
        return getId() != null && getId().equals(((Aula) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aula{" +
            "id=" + getId() +
            ", tituloAula='" + getTituloAula() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", linkVideo='" + getLinkVideo() + "'" +
            ", linkArquivos='" + getLinkArquivos() + "'" +
            ", resumo='" + getResumo() + "'" +
            "}";
    }
}
