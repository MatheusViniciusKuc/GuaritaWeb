package br.edu.ifpr.irati.ads.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

@Entity
public class Servidor implements Serializable {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "seq-serv")
    @SequenceGenerator(name = "seq-serv",
            sequenceName = "SEQ_SERV", allocationSize = 1, initialValue = 1)
    private Integer id;
    @NotBlank
    @Column(name = "siape", nullable = false, unique = true)
    private String siape;
    @Embedded
    private DadosPessoais dadosPessoais;
    private Boolean excluido;

    public Servidor() {
        this.id = 0;
        this.siape = "";
        this.dadosPessoais = new DadosPessoais();
        this.excluido = false;
    }

    public Servidor(Integer id, String siape, DadosPessoais dadosPessoais) {
        this.id = id;
        this.siape = siape;
        this.dadosPessoais = dadosPessoais;
        this.excluido = false;
    }

    public void excluir() {
        this.excluido = true;
    }

    @Override
    public String toString() {
        return dadosPessoais.getNome() + " - " + getSiape();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public DadosPessoais getDadosPessoais() {
        return dadosPessoais;
    }

    public void setDadosPessoais(DadosPessoais dadosPessoais) {
        this.dadosPessoais = dadosPessoais;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }
}
