package br.com.basis.abaco.domain;

import br.com.basis.dynamicexports.pojo.ReportObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BaseLineSintetico.
 */
@Entity
@Table(name = "baseline_sintetico")
public class BaseLineSintetico implements Serializable, ReportObject {

    @Id
    @Column(name = "row_number")
    private Long id;

    @Column(name = "id_sistema")
    private Long idsistema;

    @Column(name = "equipe_responsavel_id")
    private Long equipeResponsavelId;

    @Column(name = "nome_equipe")
    private String nomeEquipe;

    @Column(name = "sigla")
    private String sigla;

    @Column(name = "nome")
    private String nome;

    @Column(name = "numero_ocorrencia")
    private String numeroocorrencia;

    @Column(name = "sum", precision = 10, scale = 2)
    private BigDecimal sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdsistema() {
        return idsistema;
    }

    public void setIdsistema(Long idsistema) {
        this.idsistema = idsistema;
    }

    public Long getEquipeResponsavelId() {
        return equipeResponsavelId;
    }

    public void setEquipeResponsavelId(Long equipeResponsavelId) {
        this.equipeResponsavelId = equipeResponsavelId;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public void setNomeEquipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroocorrencia() {
        return numeroocorrencia;
    }

    public void setNumeroocorrencia(String numeroocorrencia) {
        this.numeroocorrencia = numeroocorrencia;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
