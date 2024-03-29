package br.com.basis.abaco.domain;

import br.com.basis.abaco.domain.enumeration.ImpactoFatorAjuste;
import br.com.basis.abaco.domain.enumeration.TipoFuncaoDados;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A FuncaoDados.
 */
@Entity
@Table(name = "funcao_dados")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "funcaodados")
public class FuncaoDados extends FuncaoAnalise implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String FUNCAODADOS = "funcaoDados";

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoFuncaoDados tipo;

    @OneToMany(mappedBy = FUNCAODADOS)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Funcionalidade> funcionalidades = new HashSet<>();

    @Column
    private String retStr;

    @Column
    private Integer quantidade;

    @JsonManagedReference(value = FUNCAODADOS)
    @OneToMany(mappedBy = FUNCAODADOS, cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Rlr> rlrs = new HashSet<>();

    @ManyToOne
    private Alr alr;

    @OneToMany(mappedBy = FUNCAODADOS, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadedFile> files = new ArrayList<>();

    @Transient
    private Set<String> rlrValues;

    @JsonManagedReference(value = FUNCAODADOS)
    @OneToMany(mappedBy = FUNCAODADOS, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Der> ders = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    private FuncaoDadosVersionavel funcaoDadosVersionavel;
    
    @Enumerated(EnumType.STRING)
    @Column(name="impacto")
    private ImpactoFatorAjuste impacto;
    
    public TipoFuncaoDados getTipo() {
        return tipo;
    }

    public FuncaoDados tipo(TipoFuncaoDados tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoFuncaoDados tipo) {
        this.tipo = tipo;
    }

    public Set<Funcionalidade> getFuncionalidades() {
        return Optional.ofNullable(this.funcionalidades)
            .map(lista -> new LinkedHashSet<Funcionalidade>(lista))
            .orElse(new LinkedHashSet<Funcionalidade>());
    }

    public FuncaoDados funcionalidades(Set<Funcionalidade> funcionalidades) {
        this.funcionalidades = Optional.ofNullable(funcionalidades)
            .map(lista -> new LinkedHashSet<Funcionalidade>(lista))
            .orElse(new LinkedHashSet<Funcionalidade>());
        return this;
    }

    public FuncaoDados addFuncionalidade(Funcionalidade funcionalidade) {
        if (funcionalidade == null) {
            return this;
        }
        this.funcionalidades.add(funcionalidade);
        funcionalidade.setFuncaoDados(this);
        return this;
    }

    public FuncaoDados removeFuncionalidade(Funcionalidade funcionalidade) {
        if (funcionalidade == null) {
            return this;
        }
        this.funcionalidades.remove(funcionalidade);
        funcionalidade.setFuncaoDados(null);
        return this;
    }

    public void setFuncionalidades(Set<Funcionalidade> funcionalidades) {
        this.funcionalidades = Optional.ofNullable(funcionalidades)
            .map(lista -> new LinkedHashSet<Funcionalidade>(lista))
            .orElse(new LinkedHashSet<Funcionalidade>());
    }

    public Set<Rlr> getRlrs() {
        return Optional.ofNullable(this.rlrs)
            .map(lista -> new LinkedHashSet<Rlr>(lista))
            .orElse(new LinkedHashSet<Rlr>());
    }

    public FuncaoDados rlrs(Set<Rlr> rlrs) {
        this.rlrs = Optional.ofNullable(rlrs)
            .map(lista -> new LinkedHashSet<Rlr>(lista))
            .orElse(new LinkedHashSet<Rlr>());
        return this;
    }

    public FuncaoDados addRlr(Rlr rlr) {
        if (rlr == null) {
            return this;
        }
        this.rlrs.add(rlr);
        rlr.setFuncaoDados(this);
        return this;
    }

    public FuncaoDados removeRlr(Rlr rlr) {
        if (rlr == null) {
            return this;
        }
        this.rlrs.remove(rlr);
        rlr.setFuncaoDados(null);
        return this;
    }

    public void setRlrs(Set<Rlr> rlrs) {
        this.rlrs = Optional.ofNullable(rlrs)
            .map(lista -> new LinkedHashSet<Rlr>(lista))
            .orElse(new LinkedHashSet<Rlr>());
    }

    public Alr getAlr() {
        return alr;
    }

    public FuncaoDados alr(Alr alr) {
        this.alr = alr;
        return this;
    }

    public void setAlr(Alr alr) {
        this.alr = alr;
    }

    public String getRetStr() {
        return retStr;
    }

    public void setRetStr(String retStr) {
        this.retStr = retStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FuncaoDados funcaoDados = (FuncaoDados) o;
        if (funcaoDados.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), funcaoDados.getId());
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<UploadedFile> getFiles() {
        List<UploadedFile> cp = new ArrayList<>();
        cp.addAll(files);
        return cp;
    }

    public void setFiles(List<UploadedFile> files) {
        List<UploadedFile> cp = new ArrayList<>();
        cp.addAll(files);
        this.files = cp;
    }

    public Set<String> getRlrValues() {
        return Collections.unmodifiableSet(rlrValues);
    }

    public void setRlrValues(Set<String> rlrValues) {
        this.rlrValues = Optional.ofNullable(rlrValues)
            .map((lista) -> new HashSet<String>(lista))
            .orElse(new HashSet<String>());
    }

    public Set<Der> getDers() {
        return Collections.unmodifiableSet(ders);
    }

    public void setDers(Set<Der> ders) {
        this.ders = Optional.ofNullable(ders)
        .map((lista) -> new HashSet<Der>(lista))
        .orElse(new HashSet<Der>());
    }

    public FuncaoDadosVersionavel getFuncaoDadosVersionavel() {
        return funcaoDadosVersionavel;
    }

    public void setFuncaoDadosVersionavel(FuncaoDadosVersionavel funcaoDadosVersionavel) {
        this.funcaoDadosVersionavel = funcaoDadosVersionavel;
    }

    public ImpactoFatorAjuste getImpacto() {
        return impacto;
    }

    public void setImpacto(ImpactoFatorAjuste impacto) {
        this.impacto = impacto;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
