package br.com.basis.abaco.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A Rlr.
 */
@Entity
@Table(name = "rlr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rlr")
public class Rlr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    @Field (index = FieldIndex.not_analyzed, type = FieldType.String)
    private String nome;

    private Integer valor;

    @OneToMany(mappedBy = "rlr")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Der> ders = new HashSet<>();

    @ManyToOne
    @JsonBackReference(value = "funcaoDados")
    private FuncaoDados funcaoDados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Rlr nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Der> getDers() {
        return Optional.ofNullable(this.ders)
            .map(lista -> new LinkedHashSet<Der>(lista))
            .orElse(new LinkedHashSet<Der>());
    }

    public Rlr ders(Set<Der> ders) {
        this.ders = Optional.ofNullable(ders)
            .map(lista -> new LinkedHashSet<Der>(lista))
            .orElse(new LinkedHashSet<Der>());
        return this;
    }

    public Rlr addDer(Der der) {
        if (der == null) {
            return this;
        }
        this.ders.add(der);
        der.setRlr(this);
        return this;
    }

    public Rlr removeDer(Der der) {
        if (der == null) {
            return this;
        }
        this.ders.remove(der);
        der.setRlr(null);
        return this;
    }

    public void setDers(Set<Der> ders) {
        this.ders = Optional.ofNullable(ders)
            .map(lista -> new LinkedHashSet<Der>(lista))
            .orElse(new LinkedHashSet<Der>());
    }

    public FuncaoDados getFuncaoDados() {
        return funcaoDados;
    }

    public Rlr funcaoDados(FuncaoDados funcaoDados) {
        this.funcaoDados = funcaoDados;
        return this;
    }

    public void setFuncaoDados(FuncaoDados funcaoDados) {
        this.funcaoDados = funcaoDados;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rlr rlr = (Rlr) o;
        if (rlr.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rlr.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Rlr{" + "id=" + id + ", nome='" + nome + "'" + '}';
    }
}
