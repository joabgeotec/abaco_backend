package br.com.basis.abaco.domain;

import br.com.basis.abaco.config.Constants;
import br.com.basis.abaco.security.AuthoritiesConstants;
import br.com.basis.dynamicexports.pojo.ReportObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "user")
public class User extends AbstractAuditingEntity implements Serializable, ReportObject {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Pattern(regexp = Constants.LOGIN_REGEX)
  @Size(min = 1, max = 100)
  @Column(length = 100, unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash", length = 60)
  private String password;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
    @Field (index = FieldIndex.not_analyzed, type = FieldType.String)
  private String lastName;

  @Email
  @Size(max = 100)
  @Column(length = 100, unique = true)
    @Field (index = FieldIndex.not_analyzed, type = FieldType.String)
  private String email;

  @NotNull
  @Column(nullable = false)
  private boolean activated = true;

  @Size(min = 2, max = 5)
  @Column(name = "lang_key", length = 5)
  private String langKey;

  @Size(max = 256)
  @Column(name = "image_url", length = 256)
  private String imageUrl;

  @Size(max = 20)
  @Column(name = "activation_key", length = 20)
  @JsonIgnore
  private String activationKey;

  @Size(max = 20)
  @Column(name = "reset_key", length = 20)
  private String resetKey;

  @Column(name = "reset_date")
  private ZonedDateTime resetDate = null;

  @ManyToMany
  @JoinTable(name = "jhi_user_authority", joinColumns = {
      @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
          @JoinColumn(name = "authority_name", referencedColumnName = "name") })
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @BatchSize(size = 20)
  private Set<Authority> authorities = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "user_tipo_equipe", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tipo_equipe_id", referencedColumnName = "id"))
  private Set<TipoEquipe> tipoEquipes = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "user_organizacao", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "organizacao_id", referencedColumnName = "id"))
  private Set<Organizacao> organizacoes = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  // Lowercase the login before saving it in database
  public void setLogin(String login) {
    this.login = login.toLowerCase(Locale.ENGLISH);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public boolean getActivated() {
    return activated;
  }

  public String getAtivoString() {
      if (getActivated()) {
          return "Sim";
        }
        return "Não";
    }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public String getResetKey() {
    return resetKey;
  }

  public void setResetKey(String resetKey) {
    this.resetKey = resetKey;
  }

  public ZonedDateTime getResetDate() {
    return resetDate;
  }

  public void setResetDate(ZonedDateTime resetDate) {
    this.resetDate = resetDate;
  }

  public String getLangKey() {
    return langKey;
  }

  public void setLangKey(String langKey) {
    this.langKey = langKey;
  }

  public Set<Authority> getAuthorities() {
      Set<Authority> authoritiesCopy = new LinkedHashSet<>();
      authoritiesCopy.addAll(authorities);
    return authoritiesCopy;
  }

    public String getAuthDescription(){

        String ponto = ". ";
        String perfil = "";

        if (authorities != null) {
            for (Authority authority : authorities) {
                perfil = perfil.concat(authority.getDescription()).concat(ponto);
            }
        }

        return perfil;

    }

  public void setAuthorities(Set<Authority> authorities) {
    Set<Authority> copy = new LinkedHashSet<>();
    copy.addAll(authorities);
    this.authorities = copy;
  }

  public Set<TipoEquipe> getTipoEquipes() {
    Set<TipoEquipe> tipoEquipeAux;
    tipoEquipeAux = tipoEquipes;
    return tipoEquipeAux;
  }

    public String getNomeEquipe(){

        String ponto = ". ";
        String nomeEquipe = "";

        if (tipoEquipes != null) {
            for (TipoEquipe equipe : tipoEquipes) {
                nomeEquipe = nomeEquipe.concat(equipe.getNome()).concat(ponto);
            }
        }

        return nomeEquipe;

    }

  public void setTipoEquipes(Set<TipoEquipe> equipes) {
    Set<TipoEquipe> tipoEquipeAux;
    tipoEquipeAux = equipes;
    this.tipoEquipes = tipoEquipeAux;
  }

  public Set<Organizacao> getOrganizacoes() {
      Set<Organizacao> organizacoesCopy = new LinkedHashSet<>();
      organizacoesCopy.addAll(organizacoes);
    return organizacoesCopy;
  }

    public String getNomeOrg(){
        String ponto = ". ";
        String nomeOrg = "";

        if (organizacoes != null) {
            for (Organizacao org : organizacoes) {
                nomeOrg = nomeOrg.concat(org.getNome()).concat(ponto);
            }
        }

        return nomeOrg;
    }

  public void setOrganizacoes(Set<Organizacao> organizacoes) {
      Set<Organizacao> copy = new LinkedHashSet<>();
      copy.addAll(organizacoes);
      this.organizacoes = copy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return login.equals(user.login);
  }

  @Override
  public int hashCode() {
    return login.hashCode();
  }

  @Override
  public String toString() {
    return "User{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
        + '\'' + ", email='" + email + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated='" + activated
        + '\'' + ", langKey='" + langKey + '\'' + ", activationKey='" + activationKey + '\'' + "}";
  }

    /**
     * Verifica se o usuário logado tem permissões de admin ou gestor
     * @return Retorna verdadeiro se tem permissões de admin ou gestor. Retorna falso em qualquer outro caso
     */
    public boolean verificarAuthority() {
        boolean temResposta = false;
    Iterator<Authority> i = this.getAuthorities().iterator();
    while (i.hasNext() && !temResposta) {
      Authority a = i.next();
      temResposta = (a.contain(AuthoritiesConstants.ADMIN) || a.contain(AuthoritiesConstants.GESTOR));
        }
        return temResposta;
    }

}
