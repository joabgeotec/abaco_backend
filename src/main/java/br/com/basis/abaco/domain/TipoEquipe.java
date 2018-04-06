package br.com.basis.abaco.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A TipoEquipe.
 */
@Entity
@Table(name = "tipo_equipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tipoequipe")
public class TipoEquipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;
    
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tipoequipe_organizacao", joinColumns = @JoinColumn(name = "tipoequipe_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "organizacao_id", referencedColumnName = "id"))
	private Set<Organizacao> organizacoes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public TipoEquipe nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Set<Organizacao> getOrganizacoes() {
		return organizacoes;
	}

	public void setOrganizacoes(Set<Organizacao> organizacoes) {
		this.organizacoes = organizacoes;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoEquipe tipoEquipe = (TipoEquipe) o;
        if (tipoEquipe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoEquipe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoEquipe{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
